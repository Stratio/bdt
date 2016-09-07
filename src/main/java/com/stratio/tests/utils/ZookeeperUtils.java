package com.stratio.tests.utils;

import org.I0Itec.zkclient.ZkConnection;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;


import java.nio.charset.StandardCharsets;

public class ZookeeperUtils {


	private final Logger logger = LoggerFactory.getLogger(ZookeeperUtils.class);


	private String zk_hosts;
	private int timeout;
	private ZkConnection client;
	private Stat st;
	private Watcher watcher = watchedEvent -> {};


	public ZookeeperUtils(String hosts, int timeout) {
		this.zk_hosts = hosts;
		this.timeout = timeout;
	}

	public void connectZk() {
		this.client = new ZkConnection(this.zk_hosts,this.timeout);
		this.client.connect(watcher);
	}

	public String zRead(String path) throws KeeperException, InterruptedException {
		logger.debug("Trying to read data at {}", path);
		byte[] b;
		String data;
		this.st = new Stat();

		b = this.client.readData(path,st,false);
		data = new String(b, StandardCharsets.UTF_8);

		logger.debug("Requested path {} contains {}", path, data);

		return data;
	}


	public void zCreate(String path, String document, boolean isEphemeral) throws KeeperException, InterruptedException {
		byte[] bDoc = document.getBytes(StandardCharsets.UTF_8);
		if(isEphemeral){
			this.client.create(path,bDoc, CreateMode.EPHEMERAL);
		}else{
			this.client.create(path,bDoc, CreateMode.PERSISTENT);
		}
	}

	public void zCreate(String path, boolean isEphemeral) throws KeeperException, InterruptedException {
		byte[] bDoc = "".getBytes(StandardCharsets.UTF_8);
		if(isEphemeral){
			this.client.create(path,bDoc, CreateMode.EPHEMERAL);
		}else{
			this.client.create(path,bDoc, CreateMode.PERSISTENT);
		}
	}

	public Boolean isConnected(){
		if ("".equals(this.client.getServers())){
			return false;
		}else{
			return true;
		}
	}

	public void delete(String path) throws KeeperException, InterruptedException {
		this.client.delete(path);
	}

	public void disconnect() throws InterruptedException {
		this.client.close();
	}
}