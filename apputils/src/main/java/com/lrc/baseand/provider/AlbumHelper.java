package com.lrc.baseand.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import com.lrc.baseand.entity.ImageBucket;
import com.lrc.baseand.entity.ImageItem;

/**
 * 本地相册帮助类
 * 
 * @author ganyu
 */
public class AlbumHelper {
	private Context mContext;
	private ContentResolver mContentResolver;
	private boolean mHasCreateAlbumItemList = false;

	public AlbumHelper(Context context) {
		this.mContext = context;
		this.mContentResolver = mContext.getContentResolver();
	}
	
	/**
	 * 获取缩略图数据
	 * 
	 * @return
	 */
	public HashMap<String, String> getThumbnailMap() {
		HashMap<String, String> thumbnailMap = new HashMap<String, String>();
		String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA };
		Cursor cursor = mContentResolver.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
		if (cursor.moveToFirst()) {
//			int _id;
			int image_id;
			String image_path;
//			int _idColumn = cursor.getColumnIndex(Thumbnails._ID);
			int image_idColumn = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cursor.getColumnIndex(Thumbnails.DATA);
			do {
//				_id = cursor.getInt(_idColumn);
				image_id = cursor.getInt(image_idColumn);
				image_path = cursor.getString(dataColumn);

				thumbnailMap.put("" + image_id, image_path);
			} while (cursor.moveToNext());
		}
		return thumbnailMap;
	}
	
	/**
	 * 获取图片集列表
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> getAlbumList() {
		List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
		String[] projection = { Albums._ID, Albums.ALBUM, Albums.ALBUM_ART, Albums.ALBUM_KEY, Albums.ARTIST, Albums.NUMBER_OF_SONGS };
		Cursor cursor = mContentResolver.query(Albums.EXTERNAL_CONTENT_URI, projection, null, null, null);
		if (cursor.moveToFirst()) {
			int _id;
			String album;
			String albumArt;
			String albumKey;
			String artist;
			int numOfSongs;

			int _idColumn = cursor.getColumnIndex(Albums._ID);
			int albumColumn = cursor.getColumnIndex(Albums.ALBUM);
			int albumArtColumn = cursor.getColumnIndex(Albums.ALBUM_ART);
			int albumKeyColumn = cursor.getColumnIndex(Albums.ALBUM_KEY);
			int artistColumn = cursor.getColumnIndex(Albums.ARTIST);
			int numOfSongsColumn = cursor.getColumnIndex(Albums.NUMBER_OF_SONGS);

			do {
				_id = cursor.getInt(_idColumn);
				album = cursor.getString(albumColumn);
				albumArt = cursor.getString(albumArtColumn);
				albumKey = cursor.getString(albumKeyColumn);
				artist = cursor.getString(artistColumn);
				numOfSongs = cursor.getInt(numOfSongsColumn);

				HashMap<String, String> hash = new HashMap<String, String>();
				hash.put("_id", _id + "");
				hash.put("album", album);
				hash.put("albumArt", albumArt);
				hash.put("albumKey", albumKey);
				hash.put("artist", artist);
				hash.put("numOfSongs", numOfSongs + "");
				albumList.add(hash);

			} while (cursor.moveToNext());
		}
		return albumList;
	}
	
	/**
	 * 获取图片集
	 * 
	 * @param refresh
	 * @return
	 */
	public List<ImageBucket> getImageBucketList(boolean refresh) {
		HashMap<String, ImageBucket> imageBucketMap = new HashMap<String, ImageBucket>();
		if (refresh || (!refresh && !mHasCreateAlbumItemList)) {
			// 构造缩略图索引
			HashMap<String, String> thumbnailMap = getThumbnailMap();

			// 构造相册索引
			String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
					Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
					Media.SIZE, Media.BUCKET_DISPLAY_NAME };
			// 得到一个游标
			Cursor cursor = mContentResolver.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
			if (cursor.moveToFirst()) {
				// 获取指定列的索引
				int photoIDIndex = cursor.getColumnIndexOrThrow(Media._ID);
				int photoPathIndex = cursor.getColumnIndexOrThrow(Media.DATA);
//				int photoNameIndex = cursor.getColumnIndexOrThrow(Media.DISPLAY_NAME);
//				int photoTitleIndex = cursor.getColumnIndexOrThrow(Media.TITLE);
//				int photoSizeIndex = cursor.getColumnIndexOrThrow(Media.SIZE);
				int bucketDisplayNameIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
				int bucketIdIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_ID);
//				int picasaIdIndex = cursor.getColumnIndexOrThrow(Media.PICASA_ID);

				do {
					String _id = cursor.getString(photoIDIndex);
//					String name = cursor.getString(photoNameIndex);
					String path = cursor.getString(photoPathIndex);
//					String title = cursor.getString(photoTitleIndex);
//					String size = cursor.getString(photoSizeIndex);
					String bucketName = cursor.getString(bucketDisplayNameIndex);
					String bucketId = cursor.getString(bucketIdIndex);
//					String picasaId = cursor.getString(picasaIdIndex);

					ImageBucket imageBucket = imageBucketMap.get(bucketId);
					if (imageBucket == null) {
						imageBucket = new ImageBucket();
						imageBucketMap.put(bucketId, imageBucket);
						imageBucket.name = bucketName;
						imageBucket.imageItemList = new ArrayList<ImageItem>();
					}
					imageBucket.count++;
					ImageItem imageItem = new ImageItem();
					imageItem.setId(_id);
					imageItem.setImagePath(path);
					imageItem.setThumbnailPath(thumbnailMap.get(_id));
					
					imageBucket.imageItemList.add(imageItem);

				} while (cursor.moveToNext());
			}

			Iterator<Entry<String, ImageBucket>> iterator = imageBucketMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, ImageBucket> entry = (Entry<String, ImageBucket>) iterator.next();
				ImageBucket imageBucket = entry.getValue();
				for (int i = 0; i < imageBucket.imageItemList.size(); ++i) {
//					ImageItem imageItem = imageBucket.imageItemList.get(i);
				}
			}
			mHasCreateAlbumItemList = true;
		}
		List<ImageBucket> imageBucketList = new ArrayList<ImageBucket>();
		Iterator<Entry<String, ImageBucket>> iterator = imageBucketMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, ImageBucket> entry = (Entry<String, ImageBucket>) iterator.next();
			imageBucketList.add(entry.getValue());
		}
		return imageBucketList;
	}
	
	/**
	 * 获取原始图像路径
	 * 
	 * @param imageId
	 * @return
	 */
	public String getOriginalImagePath(String imageId) {
		String path = null;
		String[] projection = { Media._ID, Media.DATA };
		Cursor cursor = mContentResolver.query(Media.EXTERNAL_CONTENT_URI, projection, Media._ID + "=" + imageId, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			path = cursor.getString(cursor.getColumnIndex(Media.DATA));
		}
		return path;
	}
	
}
