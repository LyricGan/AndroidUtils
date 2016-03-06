package com.yiw.circledemo.mvp.view;

import com.yiw.circledemo.bean.User;
/**
 * 
* @ClassName: ICircleViewUpdateListener 
* @Description: view,服务器响应后更新界面 
* @author yiw
* @date 2015-12-28 下午4:13:04 
*
 */
public interface ICircleViewUpdate {
	/**
	 * 发布评论
	 */
    int TYPE_PUBLIC_COMMENT = 0;
	/**
	 * 回复评论
	 */
    int TYPE_REPLY_COMMENT = 1;
	
	void update2DeleteCircle(String circleId);

	void update2AddFavorite(int circlePosition);

	void update2DeleteFavort(int circlePosition, String favortId);

	void update2AddComment(int circlePosition, int type, User replyUser);// type: 0 发布评论  1 回复评论

	void update2DeleteComment(int circlePosition, String commentId);
}
