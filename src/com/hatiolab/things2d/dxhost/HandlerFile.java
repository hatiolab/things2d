package com.hatiolab.things2d.dxhost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hatiolab.dx.api.DxConnect;
import com.hatiolab.dx.api.EventListener;
import com.hatiolab.dx.data.FileInfoArray;
import com.hatiolab.dx.data.FilePartial;
import com.hatiolab.dx.packet.Code;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;

public class HandlerFile implements EventListener {

	Host host;
	DxConnect connect;

	HandlerFile(DxConnect connect, Host host) {
		this.connect = connect;
		this.host = host;
	}

	@Override
	public void onDxEvent(Header header, Data data) {
		switch (header.getCode()) {
		case Code.DX_FILE:
			onFilePartial(data);
			break;
		case Code.DX_FILE_LIST:
			onFileList(data);
			break;
		case Code.DX_FILE_GET:
			onGetFile(data);
			break;
		case Code.DX_FILE_DELETE:
			onDeleteFile(data);
			break;
		case Code.DX_FILE_GET_LIST:
			onGetFileList(data);
			break;
		default:
			break;

		}
	}
	
	public void onGetFileList(Data data) {
		Log.e("getfilelist", "GetFileList");
	}

	public void onFileList(Data data) {
		FileInfoArray fia = (FileInfoArray) data;
		try {
			Bundle extras = new Bundle();
			Intent intent = new Intent("fileList");
//			extras.putSerializable("vehicleImg", fia);
			intent.putExtras(extras);
			
//			Host.getContext().sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onGetFile(Data data) {
	}

	public void onDeleteFile(Data data) {
	}

	public void onFilePartial(Data data) {
		FilePartial partial = (FilePartial) data;
		try {
			String path = partial.getPath();
			int total = partial.getTotalLen();
			int begin = partial.getBegin();
			int end = partial.getEnd();

			System.out.println(String.format("%32s [%d - %d of %d]", path,
					begin, end, total));

			if (end < total - 1) {
				int nbegin = end + 1;
				int nend = (end + Data.FILE_PARTIAL_MAX_SIZE) > (total - 1) ? (total - 1)
						: (end + Data.FILE_PARTIAL_MAX_SIZE);
				connect.sendGetFile(partial.getPath(), nbegin, nend);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
