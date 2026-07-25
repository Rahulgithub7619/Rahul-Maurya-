package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.models.*
import com.example.ui.components.MealCard
import com.example.ui.theme.IndiaGreen
import com.example.ui.theme.SaffronPrimary

@Composable
fun HomeScreen(
    userProfile: UserProfile,
    meals: List<Meal>,
    cartItems: List<CartItem>,
    isHindi: Boolean,
    onNavigate: (String) -> Unit,
    onAddToCart: (Meal) -> Unit,
    onRemoveFromCart: (String) -> Unit,
    onMealClick: (Meal) -> Unit
) {
    val scrollState = rememberScrollState()
    val todaySpecials = meals.filter { it.isTodaySpecial }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(bottom = 80.dp)
    ) {
        // Top Greeting Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SaffronPrimary.copy(alpha = 0.15f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isHindi) "नमस्ते, ${userProfile.name}! 👋" else "Namaste, ${userProfile.name}! 👋",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (isHindi) "आज आप क्या खाना पसंद करेंगे?" else "What would you like to eat today?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Wallet Quick Pill
                Surface(
                    onClick = { onNavigate("profile") },
                    color = IndiaGreen.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = IndiaGreen, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "₹${userProfile.walletBalance.toInt()}",
                            fontWeight = FontWeight.ExtraBold,
                            color = IndiaGreen,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Promotional Offer Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SaffronPrimary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        color = Color.White.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isHindi) "त्योहार विशेष ऑफर 🎉" else "FESTIVAL OFFER 🎉",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isHindi) "मासिक टिफिन पर 25% की भारी छूट!" else "Get 25% OFF on Monthly Tiffin Plans!",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = if (isHindi) "कोड BALAJI20 का उपयोग करें" else "Use code BALAJI20 at checkout",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = { onNavigate("subscription") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = SaffronPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (isHindi) "देखें" else "Explore", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Categories Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            QuickCategoryItem(
                emoji = "🍲",
                label = if (isHindi) "आज का मेनू" else "Today's Menu",
                onClick = { onNavigate("menu") }
            )
            QuickCategoryItem(
                emoji = "📅",
                label = if (isHindi) "टिफिन प्लान" else "Tiffin Plans",
                onClick = { onNavigate("subscription") }
            )
            QuickCategoryItem(
                emoji = "🚚",
                label = if (isHindi) "लाइव ट्रैकिंग" else "Live Track",
                onClick = { onNavigate("cart") }
            )
            QuickCategoryItem(
                emoji = "🎁",
                label = if (isHindi) "रिफर एवं कमाएं" else "Refer & Earn",
                onClick = { onNavigate("profile") }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section Title: Today's Special
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isHindi) "आज के विशेष व्यंजन 🔥" else "Today's Special Menu 🔥",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isHindi) "शुद्ध देसी घी में ताजा पकाया गया" else "Freshly cooked in pure Desi Ghee",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(onClick = { onNavigate("menu") }) {
                Text(if (isHindi) "सभी देखें ➔" else "See All ➔", color = SaffronPrimary)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Today's Special Horizontal Scroll
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(todaySpecials) { meal ->
                val quantity = cartItems.find { it.meal.id == meal.id }?.quantity ?: 0
                MealCard(
                    meal = meal,
                    isHindi = isHindi,
                    quantityInCart = quantity,
                    onAddToCart = { onAddToCart(meal) },
                    onRemoveFromCart = { onRemoveFromCart(meal.id) },
                    onMealClick = { onMealClick(meal) },
                    modifier = Modifier.width(260.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Why Balaji Tiffin Service Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isHindi) "बालाजी टिफिन क्यों चुनें? ✨" else "Why Choose Balaji Tiffin? ✨",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FeatureIconText("🧼", if (isHindi) "100% हाइजीनिक" else "100% Hygienic")
                    FeatureIconText("🧈", if (isHindi) "शुद्ध देसी घी" else "Pure Desi Ghee")
                    FeatureIconText("🚀", if (isHindi) "समय पर डिलीवरी" else "Hot & On Time")
                    FeatureIconText("⏸️", if (isHindi) "कभी भी पॉज़ करें" else "Pause Anytime")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Direct Support Bar (WhatsApp & Call)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = IndiaGreen.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SupportAgent, contentDescription = null, tint = IndiaGreen, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isHindi) "मदद चाहिए? हमसे बात करें" else "Need Help? Contact Us",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = if (isHindi) "व्हाट्सएप एवं कॉल सहायता 24x7" else "WhatsApp & Call Support Available",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = { /* WhatsApp simulation */ },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(IndiaGreen)
                    ) {
                        Text("💬", fontSize = 18.sp)
                    }

                    IconButton(
                        onClick = { /* Call simulation */ },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SaffronPrimary)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun QuickCategoryItem(
    emoji: String,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, fontSize = 28.sp)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun FeatureIconText(emoji: String, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(emoji, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}
