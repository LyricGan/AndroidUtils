package com.lrc.baseand.provider;

import java.util.ArrayList;
import java.util.List;

import com.lrc.baseand.entity.Song;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
/**
 * 本地音乐数据帮助类
 * 
 * @author ganyu
 *
 */
public class MusicProvider {
	private Context mContext;
	private static MusicProvider mInstance;
	
	private MusicProvider(Context context) {
		this.mContext = context;
	}
	
	public static MusicProvider getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new MusicProvider(context);
		}
		return mInstance;
	}
	
	/**
	 * 读取歌曲列表
	 * @return
	 */
	public List<Song> readSongList() {
		List<Song> songList = new ArrayList<Song>();
		ContentResolver resolver = mContext.getContentResolver();
		Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			Song song = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				song = new Song();
				song.name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
				song.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
				song.duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
				song.albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
				String albumKey = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_KEY));
				ContentResolver albumResolver = mContext.getContentResolver();
				Cursor albumCursor = albumResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null,
						MediaStore.Audio.AudioColumns.ALBUM_KEY + " = ?", new String[] { albumKey }, null);
				if (albumCursor != null && albumCursor.getCount() > 0) {
					albumCursor.moveToFirst();
					song.albumCoverPath = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART));
					albumCursor.close();
				}
				songList.add(song);
			}
			cursor.close();
		}
		return songList;
	}
	
}
