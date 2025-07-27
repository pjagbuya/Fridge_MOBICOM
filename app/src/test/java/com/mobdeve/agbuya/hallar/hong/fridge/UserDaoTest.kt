package com.mobdeve.agbuya.hallar.hong.fridge
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mobdeve.agbuya.hallar.hong.fridge.dao.UserDao
import com.mobdeve.agbuya.hallar.hong.fridge.database.AppDatabase
import com.mobdeve.agbuya.hallar.hong.fridge.domain.User
import com.mobdeve.agbuya.hallar.hong.fridge.extensions.toEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.UserEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        userDao = db.userDao()
    }

    @After
    fun closeDb() = db.close()

    @Test
    fun testInsertAndGetAllUsers() = runBlocking {
        val user = UserEntity(username = "test", password = "1234")
        userDao.insertUser(user)

        val allUsers = userDao.getAllUsers()
        assertEquals(1, allUsers.size)
        assertEquals("test", allUsers.first().username)
    }


    // Run one present user
    // One non present user
    @Test
    fun testDeleteAllUsers() = runBlocking {
        val user = User(userId= 1, username = "test1", password = "1234")
        userDao.insertUser(user.toEntity())

        userDao.insertUser(User(1, "test1", "pw").toEntity())
        userDao.insertUser(User(2, "test2", "pw").toEntity())

        userDao.deleteAll()
        assertTrue(userDao.getAllUsers().isEmpty())
    }
}
