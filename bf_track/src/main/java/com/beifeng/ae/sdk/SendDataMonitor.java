package com.beifeng.ae.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 鍙戦�乽rl鏁版嵁鐨勭洃鎺ц�咃紝鐢ㄤ簬鍚姩涓�涓崟鐙殑绾跨▼鏉ュ彂閫佹暟鎹�
 * 
 * @author gerry
 *
 */
public class SendDataMonitor {
	// 鏃ュ織璁板綍瀵硅薄
	private static final Logger log = Logger.getGlobal();
	// 闃熷垪锛岀敤鎴峰瓨鍌ㄥ彂閫乽rl
	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	// 鐢ㄤ簬鍗曞垪鐨勪竴涓被瀵硅薄
	private static SendDataMonitor monitor = null;

	private SendDataMonitor() {
		// 绉佹湁鏋勯�犳柟娉曪紝杩涜鍗曞垪妯″紡鐨勫垱寤�
	}

	/**
	 * 鑾峰彇鍗曞垪鐨刴onitor瀵硅薄瀹炰緥
	 * 
	 * @return
	 */
	public static SendDataMonitor getSendDataMonitor() {
		if (monitor == null) {
			synchronized (SendDataMonitor.class) {
				if (monitor == null) {
					monitor = new SendDataMonitor();

					Thread thread = new Thread(new Runnable() {

						public void run() {
							// 绾跨▼涓皟鐢ㄥ叿浣撶殑澶勭悊鏂规硶
							SendDataMonitor.monitor.run();
						}
					});
					// 娴嬭瘯鐨勬椂鍊欙紝涓嶈缃负瀹堟姢妯″紡
					// thread.setDaemon(true);
					thread.start();
				}
			}
		}
		return monitor;
	}

	/**
	 * 娣诲姞涓�涓猽rl鍒伴槦鍒椾腑鍘�
	 * 
	 * @param url
	 * @throws InterruptedException
	 */
	public static void addSendUrl(String url) throws InterruptedException {
		getSendDataMonitor().queue.put(url);
	}

	/**
	 * 鍏蜂綋鎵ц鍙戦�乽rl鐨勬柟娉�
	 * 
	 */
	private void run() {
		while (true) {
			try {
				String url = this.queue.take();
				// 姝ｅ紡鐨勫彂閫乽rl
				HttpRequestUtil.sendData(url);
			} catch (Throwable e) {
				log.log(Level.WARNING, "鍙戦�乽rl寮傚父", e);
			}
		}
	}

	/**
	 * 鍐呴儴绫伙紝鐢ㄦ埛鍙戦�佹暟鎹殑http宸ュ叿绫�
	 * 
	 * @author gerry
	 *
	 */
	public static class HttpRequestUtil {
		/**
		 * 鍏蜂綋鍙戦�乽rl鐨勬柟娉�
		 * 
		 * @param url
		 * @throws IOException
		 */
		public static void sendData(String url) throws IOException {
			HttpURLConnection con = null;
			BufferedReader in = null;

			try {
				URL obj = new URL(url); // 鍒涘缓url瀵硅薄
				con = (HttpURLConnection) obj.openConnection(); // 鎵撳紑url杩炴帴
				// 璁剧疆杩炴帴鍙傛暟
				con.setConnectTimeout(5000); // 杩炴帴杩囨湡鏃堕棿
				con.setReadTimeout(5000); // 璇诲彇鏁版嵁杩囨湡鏃堕棿
				con.setRequestMethod("GET"); // 璁剧疆璇锋眰绫诲瀷涓篻et

				System.out.println("鍙戦�乽rl:" + url);
				// 鍙戦�佽繛鎺ヨ姹�
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				// TODO: 杩欓噷鑰冭檻鏄惁鍙互
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (Throwable e) {
					// nothing
				}
				try {
					con.disconnect();
				} catch (Throwable e) {
					// nothing
				}
			}
		}
	}
}
