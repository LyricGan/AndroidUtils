package com.lyricgan.demo.util.widget.photoview;

import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Provided default implementation of GestureDetector.OnDoubleTapListener, to be overriden with custom behavior, if needed
 * <p>&nbsp;</p>
 * To be used via {@link uk.co.senab.photoview.PhotoViewAttacher#setOnDoubleTapListener(GestureDetector.OnDoubleTapListener)}
 */
public class DefaultOnDoubleTapListener implements GestureDetector.OnDoubleTapListener {
    private PhotoViewAttacher mPhotoViewAttacher;

    /**
     * Default constructor
     *
     * @param photoViewAttacher PhotoViewAttacher to bind to
     */
    public DefaultOnDoubleTapListener(PhotoViewAttacher photoViewAttacher) {
        setPhotoViewAttacher(photoViewAttacher);
    }

    /**
     * Allows to change PhotoViewAttacher within range of single instance
     *
     * @param newPhotoViewAttacher PhotoViewAttacher to bind to
     */
    public void setPhotoViewAttacher(PhotoViewAttacher newPhotoViewAttacher) {
        this.mPhotoViewAttacher = newPhotoViewAttacher;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (this.mPhotoViewAttacher == null) {
        	return false;
        }
        ImageView imageView = mPhotoViewAttacher.getImageView();
        if (null != mPhotoViewAttacher.getOnPhotoTapListener()) {
            final RectF displayRect = mPhotoViewAttacher.getDisplayRect();
            if (null != displayRect) {
                final float x = e.getX(), y = e.getY();
                // Check to see if the user tapped on the photo
                if (displayRect.contains(x, y)) {
                    float xResult = (x - displayRect.left) / displayRect.width();
                    float yResult = (y - displayRect.top) / displayRect.height();
                    mPhotoViewAttacher.getOnPhotoTapListener().onPhotoTap(imageView, xResult, yResult);
                    return true;
                }
            }
        }
        if (null != mPhotoViewAttacher.getOnViewTapListener()) {
            mPhotoViewAttacher.getOnViewTapListener().onViewTap(imageView, e.getX(), e.getY());
        }
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent ev) {
        if (mPhotoViewAttacher == null) {
        	return false;
        }
        try {
            float scale = mPhotoViewAttacher.getScale();
            float x = ev.getX();
            float y = ev.getY();
            // 判断缩放比例
            if (scale < mPhotoViewAttacher.getMediumScale()) {
                mPhotoViewAttacher.setScale(mPhotoViewAttacher.getMediumScale(), x, y, true);
            } else if (scale >= mPhotoViewAttacher.getMediumScale() && scale < mPhotoViewAttacher.getMaximumScale()) {
                mPhotoViewAttacher.setScale(mPhotoViewAttacher.getMaximumScale(), x, y, true);
            } else {
                mPhotoViewAttacher.setScale(mPhotoViewAttacher.getMinimumScale(), x, y, true);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Can sometimes happen when getX() and getY() is called
        	e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        // Wait for the confirmed onDoubleTap() instead
        return false;
    }

}
