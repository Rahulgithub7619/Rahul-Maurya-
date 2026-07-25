package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.models.*
import com.example.ui.theme.IndiaGreen
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.VegGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiffinTopBar(
    isHindi: Boolean,
    isDarkMode: Boolean,
    unreadNotifCount: Int,
    currentRole: AppRole,
    onLanguageToggle: () -> Unit,
    onThemeToggle: () -> Unit,
    onNotificationClick: () -> Unit,
    onRoleSelect: (AppRole) -> Unit
) {
    var showRoleMenu by remember { mutableStateOf(false) }

    Surface(
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SaffronPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🪔",
                            fontSize = 20.sp
                        )
                    }
                    Column {
                        Text(
                            text = if (isHindi) "बालाजी टिफिन सेवा" else "Balaji Tiffin Service",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isHindi) "शुद्ध एवं सात्विक घर का खाना" else "Pure Homestyle Indian Meals",
                            style = MaterialTheme.typography.labelSmall,
                            color = SaffronPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            },
            actions = {
                // Language Switch Chip
                AssistChip(
                    onClick = onLanguageToggle,
                    label = {
                        Text(
                            text = if (isHindi) "EN" else "हिंदी",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Language",
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = SaffronPrimary.copy(alpha = 0.12f),
                        labelColor = SaffronPrimary
                    ),
                    modifier = Modifier.height(30.dp)
                )

                IconButton(onClick = onThemeToggle) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Theme Toggle",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Notification Icon with Badge
                IconButton(onClick = onNotificationClick) {
                    BadgedBox(
                        badge = {
                            if (unreadNotifCount > 0) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = Color.White
                                ) {
                                    Text(unreadNotifCount.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // App Role Switcher Menu (Customer / Admin / Delivery)
                Box {
                    IconButton(onClick = { showRoleMenu = true }) {
                        Icon(
                            imageVector = when (currentRole) {
                                AppRole.CUSTOMER -> Icons.Default.AccountCircle
                                AppRole.ADMIN -> Icons.Default.AdminPanelSettings
                                AppRole.DELIVERY_BOY -> Icons.Default.TwoWheeler
                            },
                            contentDescription = "Role Switcher",
                            tint = SaffronPrimary
                        )
                    }

                    DropdownMenu(
                        expanded = showRoleMenu,
                        onDismissRequest = { showRoleMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(if (isHindi) "ग्राहक मोड (Customer)" else "Customer App") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            onClick = {
                                onRoleSelect(AppRole.CUSTOMER)
                                showRoleMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(if (isHindi) "एडमिन पैनल (Admin)" else "Admin Dashboard") },
                            leadingIcon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null) },
                            onClick = {
                                onRoleSelect(AppRole.ADMIN)
                                showRoleMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(if (isHindi) "डिलीवरी पार्टनर (Delivery)" else "Delivery Boy App") },
                            leadingIcon = { Icon(Icons.Default.TwoWheeler, contentDescription = null) },
                            onClick = {
                                onRoleSelect(AppRole.DELIVERY_BOY)
                                showRoleMenu = false
                            }
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun CustomerBottomBar(
    currentScreen: String,
    cartItemCount: Int,
    isHindi: Boolean,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple("home", if (isHindi) "होम" else "Home", Icons.Default.Home),
            Triple("menu", if (isHindi) "मेनू" else "Today's Menu", Icons.Default.RestaurantMenu),
            Triple("subscription", if (isHindi) "सब्स्क्रिप्शन" else "Tiffin Plans", Icons.Default.CalendarMonth),
            Triple("cart", if (isHindi) "कार्ट" else "Cart", Icons.Default.ShoppingCart),
            Triple("profile", if (isHindi) "प्रोफाइल" else "Profile", Icons.Default.Person)
        )

        items.forEach { (route, label, icon) ->
            val isSelected = currentScreen == route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(route) },
                icon = {
                    if (route == "cart" && cartItemCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = SaffronPrimary,
                                    contentColor = Color.White
                                ) {
                                    Text(cartItemCount.toString())
                                }
                            }
                        ) {
                            Icon(icon, contentDescription = label)
                        }
                    } else {
                        Icon(icon, contentDescription = label)
                    }
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SaffronPrimary,
                    selectedTextColor = SaffronPrimary,
                    indicatorColor = SaffronPrimary.copy(alpha = 0.15f)
                )
            )
        }
    }
}

@Composable
fun MealCard(
    meal: Meal,
    isHindi: Boolean,
    quantityInCart: Int,
    onAddToCart: () -> Unit,
    onRemoveFromCart: () -> Unit,
    onMealClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onMealClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                AsyncImage(
                    model = meal.imageUrl,
                    contentDescription = meal.titleEn,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Today's Special Badge
                if (meal.isTodaySpecial) {
                    Surface(
                        color = SaffronPrimary,
                        shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = if (isHindi) "🔥 आज का खास" else "🔥 Today's Special",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Veg Tag Icon Top Right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                        .background(Color.White, RoundedCornerShape(4.dp))
                        .border(1.5.dp, VegGreen, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(VegGreen)
                    )
                }

                // Rating Badge Bottom Left
                Surface(
                    color = Color.Black.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${meal.rating} (${meal.reviewsCount})",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = if (isHindi) meal.titleHi else meal.titleEn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isHindi) meal.descriptionHi else meal.descriptionEn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "₹${meal.price.toInt()}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = IndiaGreen
                        )
                        Text(
                            text = "${meal.calories} kcal",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (quantityInCart == 0) {
                        Button(
                            onClick = onAddToCart,
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isHindi) "जोड़ें" else "ADD", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(SaffronPrimary.copy(alpha = 0.15f))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            IconButton(
                                onClick = onRemoveFromCart,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = SaffronPrimary)
                            }
                            Text(
                                text = quantityInCart.toString(),
                                fontWeight = FontWeight.Bold,
                                color = SaffronPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(
                                onClick = onAddToCart,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase", tint = SaffronPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: OrderStatus, isHindi: Boolean) {
    val (bgColor, textColor, labelEn, labelHi) = when (status) {
        OrderStatus.CONFIRMED -> Quadruple(Color(0xFFE3F2FD), Color(0xFF1976D2), "Confirmed", "पुष्टि की गई")
        OrderStatus.PREPARING -> Quadruple(Color(0xFFFFF3E0), Color(0xFFE65100), "Preparing", "तैयार हो रहा है")
        OrderStatus.PACKED -> Quadruple(Color(0xFFEDE7F6), Color(0xFF512DA8), "Packed in Tiffin", "टिफिन में पैक")
        OrderStatus.OUT_FOR_DELIVERY -> Quadruple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "Out for Delivery", "डिलीवरी के लिए निकला")
        OrderStatus.DELIVERED -> Quadruple(Color(0xFFE8F5E9), Color(0xFF1B5E20), "Delivered", "डिलीवर हो गया")
        OrderStatus.CANCELLED -> Quadruple(Color(0xFFFFEBEE), Color(0xFFC62828), "Cancelled", "रद्द किया गया")
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = if (isHindi) labelHi else labelEn,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun InvoiceDialog(
    order: Order,
    isHindi: Boolean,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Stamp
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "BALAJI TIFFIN SERVICE",
                            fontWeight = FontWeight.Black,
                            color = SaffronPrimary,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "GSTIN: 08AAACB1234F1Z5",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        color = IndiaGreen.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "PAID INVOICE",
                            color = IndiaGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // Invoice Meta Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(if (isHindi) "ऑर्डर नंबर:" else "Invoice No:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("#" + order.id, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(if (isHindi) "दिनांक:" else "Date:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(order.createdAt, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Customer Delivery Address
                Text(
                    text = if (isHindi) "डिलीवरी पता:" else "Delivered To:",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = order.deliveryAddress,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Items Table
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(if (isHindi) "आइटम" else "Item Description", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Text(if (isHindi) "मात्रा x मूल्य" else "Qty x Price", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    order.items.forEach { cartItem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isHindi) cartItem.meal.titleHi else cartItem.meal.titleEn,
                                fontSize = 11.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${cartItem.quantity} x ₹${cartItem.meal.price.toInt()} = ₹${(cartItem.quantity * cartItem.meal.price).toInt()}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Summary Calculation
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(if (isHindi) "उप-योग" else "Item Total", fontSize = 12.sp)
                        Text("₹${order.itemTotal.toInt()}", fontSize = 12.sp)
                    }
                    if (order.discount > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(if (isHindi) "कूपन छूट" else "Coupon Discount", fontSize = 12.sp, color = IndiaGreen)
                            Text("-₹${order.discount.toInt()}", fontSize = 12.sp, color = IndiaGreen)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(if (isHindi) "डिलीवरी शुल्क" else "Delivery Charges", fontSize = 12.sp)
                        Text(if (order.deliveryFee == 0.0) "FREE" else "₹${order.deliveryFee.toInt()}", fontSize = 12.sp)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isHindi) "कुल भुगतान" else "Grand Total Paid",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "₹${order.totalAmount.toInt()}",
                            fontWeight = FontWeight.ExtraBold,
                            color = SaffronPrimary,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isHindi) "बंद करें" else "Close")
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isHindi) "शेयर बिल" else "Share PDF")
                    }
                }
            }
        }
    }
}
