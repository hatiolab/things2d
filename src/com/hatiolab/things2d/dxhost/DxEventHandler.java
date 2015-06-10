package com.hatiolab.things2d.dxhost;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.hatiolab.dx.api.DxConnect;
import com.hatiolab.dx.net.PacketEventListener;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;
import com.hatiolab.dx.packet.Type;

public class DxEventHandler implements PacketEventListener {
	
	Host	host;
	
	PacketEventListener handlerHeartBeat;
	PacketEventListener handlerCommand;
	PacketEventListener handlerGetSetting;
	PacketEventListener handlerSetSetting;
	PacketEventListener handlerGetState;
	PacketEventListener handlerSetState;
	PacketEventListener handlerEvent;
	PacketEventListener handlerFile;
	PacketEventListener handlerStream;
	
	public DxEventHandler(Host host) {
		this.host = host;
		
		setHandlerCommand(new HandlerCommand(host));
		setHandlerGetSetting(new HandlerGetSetting(host));
		setHandlerSetSetting(new HandlerSetSetting(host));
		setHandlerGetState(new HandlerGetState(host));
		setHandlerSetState(new HandlerSetState(host));
		setHandlerHeartBeat(new HandlerHeartBeat(host));
		setHandlerEvent(new HandlerEvent(host));
		setHandlerFile(new HandlerFile(host));
		setHandlerStream(new HandlerStream(host));
	}
	
	public PacketEventListener getHandlerHeartBeat() {
		return handlerHeartBeat;
	}

	public void setHandlerHeartBeat(PacketEventListener handlerHeartBeat) {
		this.handlerHeartBeat = handlerHeartBeat;
	}

	public PacketEventListener getHandlerCommand() {
		return handlerCommand;
	}

	public void setHandlerCommand(PacketEventListener handlerCommand) {
		this.handlerCommand = handlerCommand;
	}

	public PacketEventListener getHandlerGetSetting() {
		return handlerGetSetting;
	}

	public void setHandlerGetSetting(PacketEventListener handlerGetSetting) {
		this.handlerGetSetting = handlerGetSetting;
	}

	public PacketEventListener getHandlerSetSetting() {
		return handlerSetSetting;
	}

	public void setHandlerSetSetting(PacketEventListener handlerSetSetting) {
		this.handlerSetSetting = handlerSetSetting;
	}

	public PacketEventListener getHandlerGetState() {
		return handlerGetState;
	}

	public void setHandlerGetState(PacketEventListener handlerGetState) {
		this.handlerGetState = handlerGetState;
	}

	public PacketEventListener getHandlerSetState() {
		return handlerSetState;
	}

	public void setHandlerSetState(PacketEventListener handlerSetState) {
		this.handlerSetState = handlerSetState;
	}

	public PacketEventListener getHandlerEvent() {
		return handlerEvent;
	}

	public void setHandlerEvent(PacketEventListener handlerEvent) {
		this.handlerEvent = handlerEvent;
	}

	public PacketEventListener getHandlerFile() {
		return handlerFile;
	}

	public void setHandlerFile(PacketEventListener handlerFile) {
		this.handlerFile = handlerFile;
	}
	
	public PacketEventListener getHandlerStream() {
		return handlerStream;
	}

	public void setHandlerStream(PacketEventListener handlerStream) {
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
