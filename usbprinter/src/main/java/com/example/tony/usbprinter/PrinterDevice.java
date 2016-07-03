package com.example.tony.usbprinter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbRequest;
import android.util.Log;

public class PrinterDevice {
	private static final String LOG_TAG = "PrinterDevice";
	
	private final MainActivity mActivity;

	private final UsbDeviceConnection mDeviceConnection;
	private final UsbEndpoint mEndpointOut;
	private final UsbEndpoint mEndpointIn;
	private String mSerial;

	private final WaiterThread mWaiterThread = new WaiterThread();

	// pool of requests for the OUT endpoint
	private final LinkedList<UsbRequest> mOutRequestPool = new LinkedList<UsbRequest>();
	// pool of requests for the IN endpoint
	private final LinkedList<UsbRequest> mInRequestPool = new LinkedList<UsbRequest>();

	/**
	 * 获取打印机型号等信息
	 * @param activity MainActivity
	 * @param connection UsbDeviceConnection
	 * @param intf UsbInterface
	 */
	public PrinterDevice(MainActivity activity, UsbDeviceConnection connection,
			UsbInterface intf) {
        mActivity = activity;
		mDeviceConnection = connection;
		mSerial = connection.getSerial();

		UsbEndpoint epOut = null;
		UsbEndpoint epIn = null;
		// look for our bulk endpoints
		for (int i = 0; i < intf.getEndpointCount(); i++) {
			UsbEndpoint ep = intf.getEndpoint(i);
			if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
				if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
					epOut = ep;
				} else {
					epIn = ep;
				}
			}
		}
		if (epOut == null || epIn == null) {
			throw new IllegalArgumentException("not all endpoints found");
		}
		mEndpointOut = epOut;
		mEndpointIn = epIn;
	}

	public void start(InputStream in) {
		mWaiterThread.start();
	}

	private class WaiterThread extends Thread {
		public void run(InputStream in) {
			BufferedInputStream bufferInputStream = null;
			
			try {
				final int MAX_USBFS_BUFFER_SIZE = 16384; //16KiB.
				//File pcl3gui = new File("/sdcard/HelloWorld.PCL3GUI");
				byte[] bytes = new byte[MAX_USBFS_BUFFER_SIZE];
				bufferInputStream = new BufferedInputStream(null);
				int bytesRead = 0;
				int bytesWrite = 0;
				//从文件中按字节读取内容，到文件尾部时read方法将返回-1
	            while ((bytesRead = bufferInputStream.read(bytes)) != -1) {

	                //将读取的字节转为字符串对象
	                //String chunk = new String(bytes, 0, bytesRead);
	                //Log.i("LOG_TAG", chunk);
	                
	            	bytesWrite = mDeviceConnection.bulkTransfer(mEndpointOut, bytes,
	                		bytesRead, 40);
	            	
	            	Log.i(LOG_TAG, "bytesRead:" + bytesRead + " bytesWrite:" + bytesWrite);
	            	
	            	if(bytesWrite < 0) {
	            		// 暂停此线程 排除故障 暂时做到这种程度，因为对Java多线程编程不是太熟悉。
	            		// 打印被暂停时会进入到这个里面，可能引起的问题有：缺纸 卡纸等等。这个时候要有另外一个线程去获取Printer状态。
	            		Log.e(LOG_TAG, "暂停此线程 排除故障!");
	            		synchronized (this) { 
	            			wait();
	            			notifyAll();
	            		}
	            	}
	            	
	            	if(bytesWrite != bytesRead) {
	            		Log.e(LOG_TAG, "bultTransfer error!");
	            		break;
	            	}
	            	
	            	Thread.sleep(1000);
	            }
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
		        try {
		        	if(bufferInputStream != null)
		        		bufferInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

    /**
     * 写数据
     * @param in
     */
    public void write(InputStream in) {
        Runnable r = new WaiterThread2(in);
        new Thread(r).start();
    }

    /**
     * 写线程
     */
    private class WaiterThread2 implements Runnable {
        final InputStream inputStream;
        public WaiterThread2(InputStream in) {
            inputStream = in;
        }
        @Override
        public void run() {
            BufferedInputStream bufferInputStream = null;

            try {
                final int MAX_USBFS_BUFFER_SIZE = 16384; //16KiB.
                //File pcl3gui = new File("/sdcard/HelloWorld.PCL3GUI");
                byte[] bytes = new byte[MAX_USBFS_BUFFER_SIZE];
                bufferInputStream = new BufferedInputStream(inputStream);
                int bytesRead = 0;
                int bytesWrite = 0;
                //从文件中按字节读取内容，到文件尾部时read方法将返回-1
                while ((bytesRead = bufferInputStream.read(bytes)) != -1) {

                    //将读取的字节转为字符串对象
                    //String chunk = new String(bytes, 0, bytesRead);
                    //Log.i("LOG_TAG", chunk);

                    bytesWrite = mDeviceConnection.bulkTransfer(mEndpointOut, bytes,
                            bytesRead, 40);

                    Log.i(LOG_TAG, "bytesRead:" + bytesRead + " bytesWrite:" + bytesWrite);

                    if(bytesWrite < 0) {
                        // 暂停此线程 排除故障 暂时做到这种程度，因为对Java多线程编程不是太熟悉。
                        // 打印被暂停时会进入到这个里面，可能引起的问题有：缺纸 卡纸等等。这个时候要有另外一个线程去获取Printer状态。
                        Log.e(LOG_TAG, "暂停此线程 排除故障!");
                        synchronized (this) {
                            wait();
                            notifyAll();
                        }
                    }

                    if(bytesWrite != bytesRead) {
                        Log.e(LOG_TAG, "bultTransfer error!");
                        break;
                    }

                    Thread.sleep(1000);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if(bufferInputStream != null)
                        bufferInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
