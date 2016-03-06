package com.yiw.circledemo.mvp.presenter;

import com.yiw.circledemo.bean.User;
import com.yiw.circledemo.mvp.modle.CircleModel;
import com.yiw.circledemo.mvp.modle.IDataRequestListener;
import com.yiw.circledemo.mvp.view.ICircleViewUpdate;
/**
 *
 * @ClassName: CirclePresenter
 * @Description: 通知model请求服务器和通知view更新
 * @author yiw
 * @date 2015-12-28 下午4:06:03
 */
public class CirclePresenter {
	private CircleModel mCircleModel;
	private ICircleViewUpdate mCircleView;
	
	public CirclePresenter(ICircleViewUpdate view) {
        this.mCircleModel = new CircleModel();
		this.mCircleView = view;
	}

    /**
     * delete circle
     * @param circleId circle id
     */
	public void deleteCircle(final String circleId){
		mCircleModel.deleteCircle(new IDataRequestListener() {
			
			@Override
			public void loadSuccess(Object object) {
				mCircleView.update2DeleteCircle(circleId);
			}
		});
	}

    /**
     * add favor
     * @param circlePosition circle position
     */
	public void addFavort(final int circlePosition){
		mCircleModel.addFavort(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                mCircleView.update2AddFavorite(circlePosition);
            }
        });
	}

    /**
     * delete favor
     * @param circlePosition circle position
     * @param favortId favor id
     */
	public void deleteFavort(final int circlePosition, final String favortId){
		mCircleModel.deleteFavort(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                mCircleView.update2DeleteFavort(circlePosition, favortId);
				}
			});
	}
	
    /**
     * add comment
     * @param circlePosition position
     * @param type 0:add comment 1:reply comment
     * @param replyUser reply user
     */
	public void addComment(final int circlePosition, final int type, final User replyUser){
		mCircleModel.addComment(new IDataRequestListener(){

			@Override
			public void loadSuccess(Object object) {
				mCircleView.update2AddComment(circlePosition, type, replyUser);
			}
		});
	}

    /**
     * delete comment
     * @param circlePosition circle position
     * @param commentId comment id
     */
	public void deleteComment(final int circlePosition, final String commentId){
		mCircleModel.addComment(new IDataRequestListener() {

			@Override
			public void loadSuccess(Object object) {
				mCircleView.update2DeleteComment(circlePosition, commentId);
			}
		});
	}
}
