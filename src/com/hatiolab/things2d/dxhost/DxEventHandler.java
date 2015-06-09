package com.hatiolab.things2d.dxhost;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.hatiolab.dx.api.DxConnect;
import com.hatiolab.dx.api.EventListener;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;
import com.hatiolab.dx.packet.Type;

public class DxEventHandler implements EventListener {
	
	Host	host;
	DxConnect connect;
	
	EventListener handlerHeartBeat;
	EventListener handlerCommand;
	EventListener handlerGetSetting;
	EventListener handlerSetSetting;
	EventListener handlerGetState;
	EventListener handlerSetState;
	EventListener handlerEvent;
	EventListener handlerFile;
	EventListener handlerStream;
	
	public DxEventHandler(DxConnect connect, Host host) {
		this.connect = connect;
		this.host = host;
		
		setHandlerCommand(new HandlerCommand(connect, host));
		setHandlerGetSetting(new HandlerGetSetting(connect, host));
		setHandlerSetSetting(new HandlerSetSetting(connect, host));
		setHandlerGetState(new HandlerGetState(connect, host));
		setHandlerSetState(new HandlerSetState(connect, host));
		setHandlerHeartBeat(new HandlerHeartBeat(connect, host));
		setHandlerEvent(new HandlerEvent(connect, host));
		setHandlerFile(new HandlerFile(connect, host));
		setHandlerStream(new HandlerStream(connect, host));
	}
	
	public EventListener getHandlerHeartBeat() {
		return handlerHeartBeat;
	}

	public void setHandlerHeartBeat(EventListener handlerHeartBeat) {
		this.handlerHeartBeat = handlerHeartBeat;
	}

	public EventListener getHandlerCommand() {
		return handlerCommand;
	}

	public void setHandlerCommand(EventListener handlerCommand) {
		this.handlerCommand = handlerCommand;
	}

	public EventListener getHandlerGetSetting() {
		return handlerGetSetting;
	}

	public void setHandlerGetSetting(EventListener handlerGetSetting) {
		this.handlerGetSetting = handlerGetSetting;
	}

	public EventListener getHandlerSetSetting() {
		return handlerSetSetting;
	}

	public void setHandlerSetSetting(EventListener handlerSetSetting) {
		this.handlerSetSetting = handlerSetSetting;
	}

	public EventListener getHandlerGetState() {
		return handlerGetState;
	}

	public void setHandlerGetState(EventListener handlerGetState) {
		this.handlerGetState = handlerGetState;
	}

	public EventListener getHandlerSetState() {
		return handlerSetState;
	}

	public void setHandlerSetState(EventListener handlerSetState) {
		this.handlerSetState = handlerSetState;
	}

	public EventListener getHandlerEvent() {
		return handlerEvent;
	}

	public void setHandlerEvent(EventListener handlerEvent) {
		this.handlerEvent = handlerEvent;
	}

	public EventListener getHandlerFile() {
		return handlerFile;
	}

	public void setHandlerFile(EventListener handlerFile) {
		this.handlerFile = handlerFile;
	}
	
	public EventListener getHandlerStream() {
		return handlerStream;
	}

	public void setHandlerStream(EventListener handlerStream) {
		this.handlerStream = handlerStream;
	}

	public void onEvent(Header header, Data data) throws IOException {
		switch(header.getType()) {
		case Type.DX_PACKET_TYPE_HB : /* Heart Beat */
			handlerHeartBeat.onEvent(header, data);;
			break;
		case Type.DX_PACKET_TYPE_GET_SETTING	: /* Get Setting */
			handlerGetSetting.onEvent(header, data);;
			break;
		case Type.DX_PACKET_TYPE_SET_SETTING : /* Set Setting */
			handlerSetSetting.onEvent(header, data);;
			break;
		case Type.DX_PACKET_TYPE_GET_STATE : /* Get State */
			handlerGetState.onEvent(header, data);;
			break;
		case Type.DX_PACKET_TYPE_SET_STATE : /* Set State */
			handlerSetState.onEvent(header, data);;
			break;
		case Type.DX_PACKET_TYPE_EVENT : /* Event */
			handlerEvent.onEvent(header, data);;
			break;
		case Type.DX_PACKET_TYPE_COMMAND : /* Command */
			handlerCommand.onEvent(header, data);;
			break;
		case Type.DX_PACKET_TYPE_FILE : /* File Content */
			handlerFile.onEvent(header, data);;
			break;
		case Type.DX_PACKET_TYPE_STREAM : /* Streaming Content */
			handlerStream.onEvent(header, data);;
			break;
		default:
			break;
		}
	}

	@Override
	public void onConnected(SocketChannel channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected(SocketChannel channel) {
		// TODO Auto-generated method stub
		
	}

}
