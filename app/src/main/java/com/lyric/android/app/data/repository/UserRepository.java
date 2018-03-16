package com.lyric.android.app.data.repository;

import com.lyric.android.app.data.DataRepository;

/**
 * 用户数据仓库
 * @author lyricgan
 */
public class UserRepository implements DataRepository {

    private UserRepository() {
    }

    private static final class UserRepositoryHolder {
        private static final UserRepository mInstance = new UserRepository();
    }

    public static UserRepository getInstance() {
        return UserRepositoryHolder.mInstance;
    }

    @Override
    public void destroy() {
    }
}
