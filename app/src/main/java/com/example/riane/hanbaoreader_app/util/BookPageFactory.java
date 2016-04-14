package com.example.riane.hanbaoreader_app.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;

import com.example.riane.hanbaoreader_app.cache.BookDao;
import com.example.riane.hanbaoreader_app.modle.Book;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Vector;

/**
 * 
 *业务逻辑：分页的逻辑
 *计算真正显示文本的面积
 */
public class BookPageFactory {

	private static final String TAG = "MyBook";
	private File book_file = null;
	private MappedByteBuffer m_mbBuf = null;
	// 文件长度
	private int m_mbBufLen = 0;
	// 文字的启动位置
	private int m_mbBufBegin = 0;
	private int m_mbBufEnd = 0;

	private String m_strCharsetName = "GBK";
	private Bitmap m_book_bg = null;
	private int mWidth;// 屏幕宽度
	private int mHeight;// 屏幕高度
	private BookDao mBookDao;
	private Book book;
	// 因为MappedByteBuffer是同步处理的，故使用Vector
	private Vector<String> m_lines = new Vector<String>();

	private int m_fontSize = 20;
	private int m_textColor = Color.BLACK;
	//0xffff9e85
	private int m_backColor = Color.rgb(199, 237, 204); // 背景颜色
	private int marginWidth = 15; // 左右与边缘的距离
	private int marginHeight = 20; // 上下与边缘的距离

	private int mLineCount; // 每页可以显示的行数
	private float mVisibleHeight; // 绘制内容的宽
	private float mVisibleWidth; // 绘制内容的宽
	private boolean m_isfirstPage, m_islastPage;

	// private int m_nLineSpaceing = 5;

	private Paint mPaint;

	public BookPageFactory(Context context, int w, int h) {
		// 实例化Service
		//bookManager = new BookManager(context);
		mBookDao = new BookDao(context);
		mWidth = w;
		mHeight = h;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //画笔
		mPaint.setTextAlign(Align.LEFT);//左对齐
		mPaint.setTextSize(m_fontSize);//设置一个计算，计算文字的大小
		mPaint.setColor(m_textColor);
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - marginHeight * 2;//屏幕的高度-上下预留的距离
		mLineCount = (int) (mVisibleHeight / m_fontSize); // 可显示的行数:真正显示文本的行数
	}
	

	public long openBook(Book  book) throws IOException {
		this.book = book;
		book_file = new File(book.getFilePath());
		long lLen = book_file.length();
		m_mbBufLen = (int) lLen;
		m_mbBuf = new RandomAccessFile(book_file, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, lLen);
		// 把数据库的初始位置取出来赋值
		this.m_mbBufEnd = book.getBegin();//(book.getPageNo-1)*book.getPageSize
		//LogUtils.d("m_mbBufEnd   书本开始位置" + m_mbBufEnd);
		this.m_mbBufBegin = this.m_mbBufEnd;
		return lLen;
	}

	//读取指定位置的上一个段落
	protected byte[] readParagraphBack(int nFromPos) {
		int nEnd = nFromPos;
		int i;
		byte b0, b1;
		if (m_strCharsetName.equals("UTF-16LE")) {
			i = nEnd - 2;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				b1 = m_mbBuf.get(i + 1);
				if (b0 == 0x0a && b1 == 0x00 && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}

		} else if (m_strCharsetName.equals("UTF-16BE")) {
			i = nEnd - 2;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				b1 = m_mbBuf.get(i + 1);
				if (b0 == 0x00 && b1 == 0x0a && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}
		} else {
			i = nEnd - 1;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				if (b0 == 0x0a && i != nEnd - 1) {
					i++;
					break;
				}
				i--;
			}
		}
		if (i < 0)
			i = 0;
		int nParaSize = nEnd - i;
		int j;
		byte[] buf = new byte[nParaSize];
		for (j = 0; j < nParaSize; j++) {
			buf[j] = m_mbBuf.get(i + j);
		}
		return buf;
	}

	// 读取指定位置的下一个段落
	protected byte[] readParagraphForward(int nFromPos) {
		int nStart = nFromPos;
		int i = nStart;
		byte b0, b1;
		// 根据编码格式判断换行
		if (m_strCharsetName.equals("UTF-16LE")) {
			while (i < m_mbBufLen - 1) {
				b0 = m_mbBuf.get(i++);
				b1 = m_mbBuf.get(i++);
				if (b0 == 0x0a && b1 == 0x00) {
					break;
				}
			}
		} else if (m_strCharsetName.equals("UTF-16BE")) {
			while (i < m_mbBufLen - 1) {
				b0 = m_mbBuf.get(i++);
				b1 = m_mbBuf.get(i++);
				if (b0 == 0x00 && b1 == 0x0a) {
					break;
				}
			}
		} else {
			while (i < m_mbBufLen) {
				b0 = m_mbBuf.get(i++);
				if (b0 == 0x0a) {
					break;
				}
			}
		}
		int nParaSize = i - nStart;
		byte[] buf = new byte[nParaSize];
		for (i = 0; i < nParaSize; i++) {
			buf[i] = m_mbBuf.get(nFromPos + i);
		}
		return buf;
	}

	//画指定页的下一页
	protected Vector<String> pageDown() {
		mPaint.setTextSize(m_fontSize);
		mPaint.setColor(m_textColor);
		String strParagraph = "";
		Vector<String> lines = new Vector<String>();
		while (lines.size() < mLineCount && m_mbBufEnd < m_mbBufLen) {
			byte[] paraBuf = readParagraphForward(m_mbBufEnd); // 读取一个段落
			m_mbBufEnd += paraBuf.length;   //每次读取后，记录结束点位置，该位置是段落结束位置
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String strReturn = "";
			//替换回车换行符
			if (strParagraph.indexOf("\r\n") != -1) {
				strReturn = "\r\n";
				strParagraph = strParagraph.replaceAll("\r\n", "");
			} else if (strParagraph.indexOf("\n") != -1) {
				strReturn = "\n";
				strParagraph = strParagraph.replaceAll("\n", "");
			}

			if (strParagraph.length() == 0) {
				lines.add(strParagraph);
			}
			while (strParagraph.length() > 0) {
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
				lines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);  //得到剩余的文字
				if (lines.size() >= mLineCount) {
					break;
				}
			}
			//如果该页最后一段只显示了一部分，则从新定位结束点位置
			if (strParagraph.length() != 0) {
				try {
					m_mbBufEnd -= (strParagraph + strReturn).getBytes(m_strCharsetName).length;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return lines;
	}

	//得到上上页的结束位置
	protected void pageUp() {
		if (m_mbBufBegin < 0)
			m_mbBufBegin = 0;
		Vector<String> lines = new Vector<String>();
		String strParagraph = "";
		while (lines.size() < mLineCount && m_mbBufBegin > 0) {
			Vector<String> paraLines = new Vector<String>();
			byte[] paraBuf = readParagraphBack(m_mbBufBegin);
			//每次读取一段后，记录开始点位置，是段首开始的位置
			m_mbBufBegin -= paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, "pageUp->转换编码失败", e);
			}
			strParagraph = strParagraph.replaceAll("\r\n", "");
			strParagraph = strParagraph.replaceAll("\n", "");

			if (strParagraph.length() == 0) {
				paraLines.add(strParagraph);
			}
			while (strParagraph.length() > 0) {
				//画一行文字
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
				paraLines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
			}
			lines.addAll(0, paraLines);
		}
		while (lines.size() > mLineCount) {
			try {
				m_mbBufBegin += lines.get(0).getBytes(m_strCharsetName).length;
				lines.remove(0);
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, "pageUp->记录起始点位置失败", e);
				e.printStackTrace();
			}
		}
		//上上页的结束点等于上一页的起始点
		m_mbBufEnd = m_mbBufBegin;
		return;
	}

	//当前页
	public void currentPage() throws IOException {
		m_lines.clear();
		m_lines = pageDown();
	}

	//向前翻页
	public void prePage() throws IOException{
		if (m_mbBufBegin <= 0) {
			m_mbBufBegin = 0;
			m_isfirstPage = true;
			return;
		} else
			m_isfirstPage = false;
		m_lines.clear();
		pageUp();
		m_lines = pageDown();
	}

	//向后翻页
	public void nextPage() throws IOException{
		if (m_mbBufEnd >= m_mbBufLen) {
			m_islastPage = true;
			return;
		} else
			m_islastPage = false;
		m_lines.clear();
		m_mbBufBegin = m_mbBufEnd;
		m_lines = pageDown();
	}

	// 绘制当前的进度百分比
	public void onDraw(Canvas c) {
		mPaint.setTextSize(m_fontSize);
		mPaint.setColor(m_textColor);
		if (m_lines.size() == 0) {//还没有取好数据
			m_lines = pageDown();//存储显示是文字
			Log.d(TAG, "m_lines.size() == 0");
		}
		if (m_lines.size() > 0) {
			if (m_book_bg == null)//判断背景颜色是否为空
				c.drawColor(m_backColor);
			else
				c.drawBitmap(m_book_bg, 0, 0, null);
			int y = marginHeight;
			// 循环输出文字，进行绘制
			for (String strLine : m_lines) {
				y += m_fontSize;//一次绘制一行
				c.drawText(strLine, marginWidth, y, mPaint);
				//Log.d("MyBook", strLine);
			}
		}
		//计算分页的内容，也是绘制在屏幕
		float fPercent = (float) (book.getBegin() * 1.0 / m_mbBufLen);

		DecimalFormat df = new DecimalFormat("#0.00");
		String strPercent = df.format(fPercent * 100) + "%";
		 int nPercentWidth = (int) mPaint.measureText("999.9%") + 1;
		 c.drawText(strPercent, mWidth - nPercentWidth, mHeight - 5, mPaint);
		// 修改SQList book的数据
		book.setBegin(m_mbBufBegin);
		book.setProgress(strPercent);
		book.setLastReadTime(new Date());
		mBookDao.update(book);
		Log.d(TAG, "onDraw m_mbBufBegin:" + m_mbBufBegin);
		Log.d(TAG, "onDraw m_mbBufEnd:" + m_mbBufEnd);
		Log.d(TAG, "onDraw m_mbBufLen:" + m_mbBufLen);
		Log.d(TAG, "onDraw strPercent:" + strPercent);
		// Log.d(TAG, "onDraw book:" + book);
	}

	public void setBgBitmap(Bitmap BG) {
		m_book_bg = BG;
	}

	public boolean isfirstPage() {
		return m_isfirstPage;
	}

	public boolean islastPage() {
		return m_islastPage;
	}

	public void setM_fontSize(int m_fontSize) {
		this.m_fontSize = m_fontSize;
		mLineCount = (int) (mVisibleHeight / m_fontSize) - 1;
	}

	//设置页面起始点
	public void setM_mbBufEnd(int m_mbBufEnd) {
		this.m_mbBufEnd = m_mbBufEnd;
		//this.m_mbBufBegin = m_mbBufEnd;
		this.m_lines.clear();
	}

	public int getM_backColor() {
		return m_backColor;
	}

	public void setM_backColor(int m_backColor) {
		this.m_backColor = m_backColor;
	}

	public int getM_mbBufLen() {
		return m_mbBufLen;
	}

	public void setM_mbBufLen(int m_mbBufLen) {
		this.m_mbBufLen = m_mbBufLen;
	}

	public int getM_mbBufBegin() {
		return m_mbBufBegin;
	}
	//设置页面结束点
	public void setM_mbBufBegin(int m_mbBufBegin) {
		this.m_mbBufBegin = m_mbBufBegin;
	}

	public int getM_mbBufEnd() {
		return m_mbBufEnd;
	}

	public String getFirstLineText() {
		return m_lines.size() > 0 ? m_lines.get(0) + m_lines.get(1): "";
	}
	public void setM_textColor(int m_textColor){
		this.m_textColor = m_textColor;
	}

	public int getM_textColor(){
		return m_textColor;
	}

}
