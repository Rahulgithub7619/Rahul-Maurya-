package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.models.*
import com.example.data.repository.TiffinRepository
import com.example.ui.components.CustomerBottomBar
import com.example.ui.components.TiffinTopBar
import com.example.ui.screens.*
import com.example.ui.theme.BalajiTiffinTheme
import com.example.ui.theme.IndiaGreen
import com.example.ui.theme.SaffronPrimary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BalajiTiffinApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalajiTiffinApp() {
    val repository = TiffinRepository

    val currentRole by repository.currentRole.collectAsStateWithLifecycle()
    val isHindi by repository.isHindi.collectAsStateWithLifecycle()
    val isDarkMode by repository.isDarkMode.collectAsStateWithLifecycle()
    val userProfile by repository.userProfile.collectAsStateWithLifecycle()
    val meals by repository.meals.collectAsStateWithLifecycle()
    val cartItems by repository.cartItems.collectAsStateWithLifecycle()
    val subscriptions by repository.subscriptions.collectAsStateWithLifecycle()
    val orders by repository.orders.collectAsStateWithLifecycle()
    val walletTransactions by repository.walletTransactions.collectAsStateWithLifecycle()
    val appliedCoupon by repository.appliedCoupon.collectAsStateWithLifecycle()
    val notifications by repository.notifications.collectAsStateWithLifecycle()

    var currentScreen by remember { mutableStateOf("splash") } // splash, auth, home, menu, subscription, cart, tracking, profile
    var trackedOrder by remember { mutableStateOf<Order?>(null) }
    var showNotificationSheet by remember { mutableStateOf(false) }

    val unreadNotifCount = notifications.count { !it.isRead }

    BalajiTiffinTheme(darkTheme = isDarkMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (currentRole) {
                AppRole.ADMIN -> {
                    AdminPanelScreen(
                        meals = meals,
                        orders = orders,
                        subscriptions = subscriptions,
                        isHindi = isHindi,
                        onAddMeal = { repository.addMealByAdmin(it) },
                        onToggleMeal = { repository.toggleMealAvailability(it) },
                        onDeleteMeal = { repository.deleteMealByAdmin(it) },
                        onSendNotification = { tEn, tHi, bEn, bHi ->
                            repository.sendPushNotificationByAdmin(tEn, tHi, bEn, bHi)
                        },
                        onBackToCustomer = { repository.setRole(AppRole.CUSTOMER) }
                    )
                }

                AppRole.DELIVERY_BOY -> {
                    DeliveryBoyScreen(
                        orders = orders,
                        isHindi = isHindi,
                        onUpdateStatus = { id, status -> repository.updateOrderStatus(id, status) },
                        onBackToCustomer = { repository.setRole(AppRole.CUSTOMER) }
                    )
                }

                AppRole.CUSTOMER -> {
                    when (currentScreen) {
                        "splash" -> {
                            SplashScreen(
                                isHindi = isHindi,
                                onStartClick = { currentScreen = "auth" }
                            )
                        }

                        "auth" -> {
                            AuthScreen(
                                isHindi = isHindi,
                                onLoginSuccess = { currentScreen = "home" }
                            )
                        }

                        else -> {
                            // Main App Shell with Navigation Bar & Top Header
                            Scaffold(
                                topBar = {
                                    TiffinTopBar(
                                        isHindi = isHindi,
                                        isDarkMode = isDarkMode,
                                        unreadNotifCount = unreadNotifCount,
                                        currentRole = currentRole,
                                        onLanguageToggle = { repository.toggleLanguage() },
                                        onThemeToggle = { repository.toggleDarkMode() },
                                        onNotificationClick = { showNotificationSheet = true },
                                        onRoleSelect = { repository.setRole(it) }
                                    )
                                },
                                bottomBar = {
                                    CustomerBottomBar(
                                        currentScreen = currentScreen,
                                        cartItemCount = cartItems.sumOf { it.quantity },
                                        isHindi = isHindi,
                                        onNavigate = { route -> currentScreen = route }
                                    )
                                }
                            ) { innerPadding ->
                                Box(modifier = Modifier.padding(innerPadding)) {
                                    when (currentScreen) {
                                        "home" -> {
                                            HomeScreen(
                                                userProfile = userProfile,
                                                meals = meals,
                                                cartItems = cartItems,
                                                isHindi = isHindi,
                                                onNavigate = { route -> currentScreen = route },
                                                onAddToCart = { repository.addToCart(it) },
                                                onRemoveFromCart = { repository.removeFromCart(it) },
                                                onMealClick = { currentScreen = "menu" }
                                            )
                                        }

                                        "menu" -> {
                                            MenuScreen(
                                                meals = meals,
                                                cartItems = cartItems,
                                                isHindi = isHindi,
                                                onAddToCart = { meal, note -> repository.addToCart(meal, note) },
                                                onRemoveFromCart = { id -> repository.removeFromCart(id) }
                                            )
                                        }

                                        "subscription" -> {
                                            SubscriptionScreen(
                                                plans = repository.availablePlans,
                                                subscriptions = subscriptions,
                                                userAddresses = userProfile.addresses,
                                                isHindi = isHindi,
                                                onCreateSubscription = { plan, slot, sDate, addr ->
                                                    repository.createSubscription(plan, slot, sDate, addr)
                                                },
                                                onTogglePause = { subId -> repository.togglePauseSubscription(subId) },
                                                onSkipDate = { subId, dt -> repository.skipSubscriptionDate(subId, dt) }
                                            )
                                        }

                                        "cart" -> {
                                            CartScreen(
                                                cartItems = cartItems,
                                                userProfile = userProfile,
                                                appliedCoupon = appliedCoupon,
                                                availableCoupons = repository.availableCoupons,
                                                isHindi = isHindi,
                                                onAddToCart = { repository.addToCart(it) },
                                                onRemoveFromCart = { repository.removeFromCart(it) },
                                                onApplyCoupon = { repository.applyCoupon(it) },
                                                onRemoveCoupon = { repository.removeCoupon() },
                                                onPlaceOrder = { addr, method -> repository.placeOrder(addr, method) },
                                                onOrderPlacedSuccess = { createdOrder ->
                                                    trackedOrder = createdOrder
                                                    currentScreen = "tracking"
                                                }
                                            )
                                        }

                                        "tracking" -> {
                                            val activeOrder = trackedOrder ?: orders.firstOrNull()
                                            if (activeOrder != null) {
                                                LiveTrackingScreen(
                                                    order = activeOrder,
                                                    isHindi = isHindi,
                                                    onBack = { currentScreen = "home" }
                                                )
                                            } else {
                                                currentScreen = "home"
                                            }
                                        }

                                        "profile" -> {
                                            ProfileScreen(
                                                userProfile = userProfile,
                                                walletTransactions = walletTransactions,
                                                isHindi = isHindi,
                                                isDarkMode = isDarkMode,
                                                onLanguageToggle = { repository.toggleLanguage() },
                                                onThemeToggle = { repository.toggleDarkMode() },
                                                onAddMoneyToWallet = { amt -> repository.addMoneyToWallet(amt) },
                                                onAddAddress = { addr -> repository.addAddress(addr) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Notification Bottom Sheet Drawer
            if (showNotificationSheet) {
                ModalBottomSheet(onDismissRequest = { showNotificationSheet = false }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isHindi) "सूचनाएं" else "Push Notifications",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = SaffronPrimary)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(notifications) { notif ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = if (isHindi) notif.titleHi else notif.titleEn,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = if (isHindi) notif.messageHi else notif.messageEn,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(notif.timestamp, fontSize = 10.sp, color = SaffronPrimary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
