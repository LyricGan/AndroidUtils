package com.lrc.baseand.entity;

/**
 * 歌曲实体类
 * 
 * @author ganyu
 *
 */
public class Song extends JsonHelper {
	/** 歌曲名称 */
	public String name;
	/** 音乐家即歌手名称 */
	public String artist;
	/** 专辑名称 */
	public String albumName;
	/** 专辑封面地址 */
	public String albumCoverPath;
	/** 持续时间 */
	public int duration;
	
}
