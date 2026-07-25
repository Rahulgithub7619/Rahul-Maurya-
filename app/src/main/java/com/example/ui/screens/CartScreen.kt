package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.*
import com.example.ui.theme.IndiaGreen
import com.example.ui.theme.SaffronPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    userProfile: UserProfile,
    appliedCoupon: Coupon?,
    availableCoupons: List<Coupon>,
    isHindi: Boolean,
    onAddToCart: (Meal) -> Unit,
    onRemoveFromCart: (String) -> Unit,
    onApplyCoupon: (Coupon) -> Unit,
    onRemoveCoupon: () -> Unit,
    onPlaceOrder: (String, PaymentMethod) -> Order,
    onOrderPlacedSuccess: (Order) -> Unit
) {
    var selectedPaymentMethod by remember { mutableStateOf(PaymentMethod.UPI) }
    var couponInput by remember { mutableStateOf("") }
    var selectedAddressIndex by remember { mutableIntStateOf(0) }
    var isPlacingOrder by remember { mutableStateOf(false) }

    val selectedAddressObj = userProfile.addresses.getOrNull(selectedAddressIndex)
    val deliveryAddressString = selectedAddressObj?.let { "${it.houseNo}, ${it.street}, ${it.landmark}, ${it.city} - ${it.pincode}" } ?: "Home, Jaipur"

    val subtotal = cartItems.sumOf { it.meal.price * it.quantity }
    var couponDiscount = 0.0
    if (appliedCoupon != null && subtotal >= appliedCoupon.minOrderAmount) {
        couponDiscount = (subtotal * appliedCoupon.discountPercent / 100).coerceAtMost(appliedCoupon.maxDiscount)
    }
    val deliveryFee = if (subtotal > 200 || cartItems.isEmpty()) 0.0 else 25.0
    val grandTotal = (subtotal - couponDiscount + deliveryFee).coerceAtLeast(0.0)

    if (cartItems.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🛒", fontSize = 60.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isHindi) "आपकी कार्ट खाली है" else "Your Cart is Empty",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isHindi) "स्वादिष्ट भोजन ऑर्डर करने के लिए मेनू देखें!" else "Browse our fresh homestyle menu to add meals!",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .weight(1f)
            ) {
                // Address Selection Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = SaffronPrimary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isHindi) "डिलीवरी का पता" else "Delivery Address",
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                TextButton(onClick = {
                                    selectedAddressIndex = (selectedAddressIndex + 1) % userProfile.addresses.size
                                }) {
                                    Text(if (isHindi) "बदलें" else "Change", color = SaffronPrimary)
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${selectedAddressObj?.label}: $deliveryAddressString",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Cart Items List
                item {
                    Text(
                        text = if (isHindi) "ऑर्डर किए गए आइटम (${cartItems.sumOf { it.quantity }})" else "Order Items (${cartItems.sumOf { it.quantity }})",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                items(cartItems) { item ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isHindi) item.meal.titleHi else item.meal.titleEn,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "₹${item.meal.price.toInt()} each",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (item.customNotes.isNotEmpty()) {
                                    Text(
                                        text = "Note: ${item.customNotes}",
                                        fontSize = 11.sp,
                                        color = SaffronPrimary
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { onRemoveFromCart(item.meal.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = null, tint = SaffronPrimary)
                                }
                                Text(
                                    text = item.quantity.toString(),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                IconButton(
                                    onClick = { onAddToCart(item.meal) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = SaffronPrimary)
                                }
                            }
                        }
                    }
                }

                // Coupons Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (isHindi) "कूपन एवं ऑफर" else "Coupons & Promos",
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (appliedCoupon != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(IndiaGreen.copy(alpha = 0.15f))
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("CODE: ${appliedCoupon.code}", fontWeight = FontWeight.ExtraBold, color = IndiaGreen)
                                        Text(if (isHindi) appliedCoupon.descriptionHi else appliedCoupon.descriptionEn, fontSize = 11.sp)
                                    }
                                    TextButton(onClick = onRemoveCoupon) {
                                        Text(if (isHindi) "हटाएं" else "Remove", color = Color.Red)
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = couponInput,
                                        onValueChange = { couponInput = it },
                                        placeholder = { Text("BALAJI20") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    Button(
                                        onClick = {
                                            val found = availableCoupons.find { it.code.equals(couponInput, ignoreCase = true) }
                                            if (found != null) {
                                                onApplyCoupon(found)
                                                couponInput = ""
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(if (isHindi) "लागू करें" else "APPLY")
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Fast Coupon Selector Pills
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    availableCoupons.forEach { c ->
                                        AssistChip(
                                            onClick = { onApplyCoupon(c) },
                                            label = { Text(c.code, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Payment Method Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (isHindi) "भुगतान का तरीका" else "Payment Method",
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedPaymentMethod == PaymentMethod.UPI,
                                    onClick = { selectedPaymentMethod = PaymentMethod.UPI }
                                )
                                Text("UPI / GPay / PhonePe / Paytm", fontSize = 13.sp)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedPaymentMethod == PaymentMethod.WALLET,
                                    onClick = { selectedPaymentMethod = PaymentMethod.WALLET }
                                )
                                Text("Balaji Wallet (Balance: ₹${userProfile.walletBalance.toInt()})", fontSize = 13.sp)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedPaymentMethod == PaymentMethod.CASH_ON_DELIVERY,
                                    onClick = { selectedPaymentMethod = PaymentMethod.CASH_ON_DELIVERY }
                                )
                                Text(if (isHindi) "कैश ऑन डिलीवरी (COD)" else "Cash on Delivery", fontSize = 13.sp)
                            }
                        }
                    }
                }

                // Bill Breakdown Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(if (isHindi) "बिल विवरण" else "Bill Details", fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(if (isHindi) "आइटम टोटल" else "Item Subtotal", fontSize = 12.sp)
                                Text("₹${subtotal.toInt()}", fontSize = 12.sp)
                            }

                            if (couponDiscount > 0) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(if (isHindi) "कूपन छूट" else "Coupon Discount", fontSize = 12.sp, color = IndiaGreen)
                                    Text("-₹${couponDiscount.toInt()}", fontSize = 12.sp, color = IndiaGreen)
                                }
                            }

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(if (isHindi) "डिलीवरी शुल्क" else "Delivery Fee", fontSize = 12.sp)
                                Text(if (deliveryFee == 0.0) "FREE" else "₹${deliveryFee.toInt()}", fontSize = 12.sp)
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(if (isHindi) "कुल भुगतान" else "To Pay", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("₹${grandTotal.toInt()}", fontWeight = FontWeight.Black, fontSize = 18.sp, color = SaffronPrimary)
                            }
                        }
                    }
                }
            }

            // Fixed Bottom Checkout Bar
            Surface(
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(if (isHindi) "कुल मूल्य" else "Total Amount", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("₹${grandTotal.toInt()}", fontWeight = FontWeight.Black, fontSize = 20.sp, color = IndiaGreen)
                    }

                    Button(
                        onClick = {
                            isPlacingOrder = true
                            val newOrder = onPlaceOrder(deliveryAddressString, selectedPaymentMethod)
                            onOrderPlacedSuccess(newOrder)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(50.dp)
                    ) {
                        Text(
                            text = if (isHindi) "ऑर्डर करें ➔" else "PLACE ORDER ➔",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
