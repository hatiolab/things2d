package com.hatiolab.things2d.dxhost;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Timer;
import java.util.TimerTask;

import com.hatiolab.dx.net.PacketEventListener;
import com.hatiolab.dx.net.PacketSender;
import com.hatiolab.dx.packet.Data;
import com.hatiolab.dx.packet.Header;
import com.hatiolab.dx.packet.Packet;
import com.hatiolab.dx.packet.Type;

public class DxEventHandler implements PacketEventListener {

	Host host;
	PacketSender sender;

	HeartBeatJob job = null;
	Timer scheduler = null;

	PacketEventListener handlerHeartBeat;
	PacketEventListener handlerCommand;
	PacketEventListener handlerGetSetting;
	PacketEventListener handlerSetSetting;
	PacketEventListener handlerGetState;
	PacketEventListener handlerSetState;
	PacketEventListener handlerEvent;
	PacketEventListener handlerFile;
	PacketEventListener handlerStream;

	public DxEventHandler(Host host, PacketSender sender) {
		this.host = host;
		this.sender = sender;

		setHandlerCommand(new HandlerCommand(host, sender));
		setHandlerGetSetting(new HandlerGetSetting(host, sender));
		setHandlerSetSetting(new HandlerSetSetting(host, sender));
		setHandlerGetState(new HandlerGetState(host, sender));
		setHandlerSetState(new HandlerSetState(host, sender));
		setHandlerHeartBeat(new HandlerHeartBeat(host, sender));
		setHandlerEvent(new HandlerEvent(host, sender));
		setHandlerFile(new HandlerFile(host, sender));
		setHandlerStream(new HandlerStream(host, sender));
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
		switch (header.getType()) {
		case Type.DX_PACKET_TYPE_HB: /* Heart Beat */
			handlerHeartBeat.onEvent(header, data);
			;
			break;
		case Type.DX_PACKET_TYPE_GET_SETTING: /* Get Setting */
			handlerGetSetting.onEvent(header, data);
			;
			break;
		case Type.DX_PACKET_TYPE_SET_SETTING: /* Set Setting */
			handlerSetSetting.onEvent(header, data);
			;
			break;
		case Type.DX_PACKET_TYPE_GET_STATE: /* Get State */
			handlerGetState.onEvent(header, data);
			;
			break;
		case Type.DX_PACKET_TYPE_SET_STATE: /* Set State */
			handlerSetState.onEvent(header, data);
			;
			break;
		case Type.DX_PACKET_TYPE_EVENT: /* Event */
			handlerEvent.onEvent(header, data);
			;
			break;
		case Type.DX_PACKET_TYPE_COMMAND: /* Command */
			handlerCommand.onEvent(header, data);
			;
			break;
		case Type.DX_PACKET_TYPE_FILE: /* File Content */
			handlerFile.onEvent(header, data);
			;
			break;
		case Type.DX_PACKET_TYPE_STREAM: /* Streaming Content */
			handlerStream.onEvent(header, data);
			;
			break;
		default:
			break;
		}
	}

	@Override
	public void onConnected(SocketChannel channel) {
		scheduler = new Timer();
		job = new HeartBeatJob();
		scheduler.scheduleAtFixedRate(job, 10000, 20000);
	}

	@Override
	public void onDisconnected(SocketChannel channel) {
		if (scheduler != null) {
			scheduler.cancel();
			scheduler = null;
			job = null;
		}
	}

	class HeartBeatJob extends TimerTask {
		public void run() {
			try {
				final Packet packet = new Packet(Type.DX_PACKET_TYPE_HB, 0,
						null);
				sender.sendPacket(packet);
			} catch (Exception e) {
			}
		}
	}

}
