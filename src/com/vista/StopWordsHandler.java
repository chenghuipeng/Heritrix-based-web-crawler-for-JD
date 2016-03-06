package com.vista;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * ͣ�ôʴ�����
 * 
 * @author phinecos
 * 
 */
public class StopWordsHandler
{
	ArrayList<String> stopWordsList;

	public StopWordsHandler()
	{
		stopWordsList = new ArrayList<String>();
	}

	public boolean IsStopWord(String word)
	{
		for (String string : stopWordsList)
		{
			if (string.equals(word))
				return true;
		}
		return false;
	}

	public void getStopWord()
	{

		String fileName = "/home/chris/workspace/heritrix/中文停用词库.txt";
		File file = new File(fileName);
		BufferedReader reader = null;
		try
		{
			InputStreamReader read = new InputStreamReader(new FileInputStream(
					file), "gbk");
			reader = new BufferedReader(read);
			String tempString = new String();
			while ((tempString = reader.readLine()) != null)
			{
				stopWordsList.add(tempString);
			}
			reader.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				} catch (IOException e1)
				{
				}
			}
		}
	}

}
