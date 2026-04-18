package com.team3.sneakx

import android.content.Context
import androidx.room.Room
import com.team3.sneakx.data.local.AppDatabase
import com.team3.sneakx.data.repo.AdminRepository
import com.team3.sneakx.data.repo.AuthRepository
import com.team3.sneakx.data.repo.CartRepository
import com.team3.sneakx.data.repo.ListingRepository
import com.team3.sneakx.data.repo.OrderRepository
import com.team3.sneakx.data.repo.ProfileRepository
import com.team3.sneakx.data.repo.SeedConstants
import com.team3.sneakx.data.session.SessionStore
import com.team3.sneakx.data.session.ThemePreferenceStore
import com.team3.sneakx.domain.Role
import com.team3.sneakx.security.PasswordHasher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.team3.sneakx.data.local.entity.UserEntity
import java.util.UUID

class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    val database: AppDatabase = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "sneakx.db"
    ).build()

    val sessionStore = SessionStore(appContext)
    val themePreferenceStore = ThemePreferenceStore(appContext)

    val authRepository = AuthRepository(database.userDao(), sessionStore)
    val listingRepository = ListingRepository(database.listingDao())
    val cartRepository = CartRepository(database.cartDao(), database.listingDao())
    val orderRepository = OrderRepository(database.orderDao(), database.cartDao(), database.listingDao())
    val adminRepository = AdminRepository(database.userDao(), database.listingDao(), database.adminLogDao())
    val profileRepository = ProfileRepository(database.userDao())

    fun seedAdminIfNeeded() {
        CoroutineScope(Dispatchers.IO).launch {
            val email = SeedConstants.ADMIN_EMAIL.lowercase()
            if (database.userDao().getByEmail(email) != null) return@launch
            val salt = PasswordHasher.generateSalt()
            val hash = PasswordHasher.hash(SeedConstants.ADMIN_PASSWORD_PLAIN, salt)
            database.userDao().insert(
                UserEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Administrator",
                    email = email,
                    passwordHash = hash,
                    salt = salt,
                    role = Role.ADMIN.name,
                    enabled = true
                )
            )
        }
    }
}
