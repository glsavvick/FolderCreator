package com.humut.fileop;

import java.io.File;
import java.util.ArrayList;

public class UndoOperation {

	private int folderCount;
	private String path;
	private String name;
	public static ArrayList<UndoOperation> undoList = new ArrayList<>();
	
	public UndoOperation(int folderCount, String path, String name)
	{
		this.folderCount = folderCount;
		this.path = path;
		this.name = name;
	}

	public int getFolderCount() {
		return folderCount;
	}
	public void setFolderCount(int folderCount) {
		this.folderCount = folderCount;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static boolean undo()
	{
		boolean check = false;
		UndoOperation temp = undoList.get(undoList.size()-1);
		try
		{
			String path = temp.getPath();
			String name = temp.getName();
			int count = temp.getFolderCount();
			for (int i = 1; i < count + 1; i++) 
			{
				File f = new File(path + File.separator + name + " " + i);
				if(f.exists())
				{
					if(f.delete())
						check = true;
					else
					{
						check = false;
						break;
								
					}
				}
			}
			undoList.remove(temp);
			return check;
		}
		catch(Exception e)
		{
			return false;
		}
		finally
		{
			
		}
	}
	
}
