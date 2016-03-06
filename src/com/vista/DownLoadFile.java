package com.vista;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class DownLoadFile
{
	/**
	 * ���� url ����ҳ����������Ҫ�������ҳ���ļ��� ȥ���� url �з��ļ����ַ�
	 */
	public String getFileNameByUrl(String url)
	{
		// remove http://
		url = url.substring(7);
		url = url.replaceAll("[\\?/:*|<>\"]", "_");
		return url;

	}

	/**
	 * ������ҳ�ֽ����鵽�����ļ� filePath ΪҪ������ļ�����Ե�ַ
	 */
	private void saveToLocal(String cc, String filePath)
	{
		try
		{
			File tofile = new File(filePath);

			if (!tofile.exists())
			{
				tofile.createNewFile();
			}
			OutputStreamWriter write = new OutputStreamWriter(
					new FileOutputStream(tofile), "gbk");
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(cc);
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/* ���� url ָ�����ҳ */
	public String downloadFile(String url, String cc)
	{
		String filePath = null;

		// ������ҳ url ���ɱ���ʱ���ļ���
		filePath = "temp\\" + getFileNameByUrl(url);
		saveToLocal(cc, filePath);

		return filePath;
	}
}
