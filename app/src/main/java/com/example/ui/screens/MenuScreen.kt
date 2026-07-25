package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ui.theme.VegGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    meals: List<Meal>,
    cartItems: List<CartItem>,
    isHindi: Boolean,
    onAddToCart: (Meal, String) -> Unit,
    onRemoveFromCart: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedMealType by remember { mutableStateOf(MealType.ALL) }
    var selectedCategory by remember { mutableStateOf(MealCategory.ALL) }
    var selectedMealForDetail by remember { mutableStateOf<Meal?>(null) }
    var customNote by remember { mutableStateOf("") }

    val filteredMeals = meals.filter { meal ->
        val matchesSearch = if (isHindi) meal.titleHi.contains(searchQuery, ignoreCase = true) || meal.descriptionHi.contains(searchQuery, ignoreCase = true)
        else meal.titleEn.contains(searchQuery, ignoreCase = true) || meal.descriptionEn.contains(searchQuery, ignoreCase = true)

        val matchesMealType = selectedMealType == MealType.ALL || meal.mealType == selectedMealType || meal.mealType == MealType.ALL
        val matchesCategory = selectedCategory == MealCategory.ALL || meal.category == selectedCategory

        matchesSearch && matchesMealType && matchesCategory
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search & Filter Header
        Surface(
            shadowElevation = 2.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(if (isHindi) "थाली, पराठा या कढ़ी खोजें..." else "Search Thali, Paratha, Dal...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SaffronPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Meal Slot Tabs (All, Breakfast, Lunch, Dinner)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val slots = listOf(
                        MealType.ALL to if (isHindi) "सभी (All)" else "All Meals",
                        MealType.BREAKFAST to if (isHindi) "नाश्ता 🌅" else "Breakfast 🌅",
                        MealType.LUNCH to if (isHindi) "दोपहर ☀️" else "Lunch ☀️",
                        MealType.DINNER to if (isHindi) "रात 🌙" else "Dinner 🌙"
                    )

                    slots.forEach { (type, label) ->
                        val isSelected = selectedMealType == type
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedMealType = type },
                            label = { Text(label, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SaffronPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Category Chips (All, Veg Thali, Special Thali, Mini Tiffin, Sweets)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val categories = listOf(
                        MealCategory.ALL to if (isHindi) "सभी श्रेणियां" else "All Categories",
                        MealCategory.SPECIAL_THALI to if (isHindi) "शाही थाली" else "Special Thali",
                        MealCategory.VEG_THALI to if (isHindi) "वेज थाली" else "Standard Veg",
                        MealCategory.MINI_TIFFIN to if (isHindi) "मिनी टिफिन" else "Mini Tiffin",
                        MealCategory.BREAKFAST to if (isHindi) "नाश्ता कॉम्बो" else "Breakfast Combo",
                        MealCategory.SWEET_DESSERT to if (isHindi) "मीठा / हलवा" else "Sweets & Desserts",
                        MealCategory.ADD_ONS to if (isHindi) "एक्स्ट्रा रोटी/चावल" else "Add-ons & Rotis"
                    )

                    items(categories) { (cat, label) ->
                        val isSelected = selectedCategory == cat
                        AssistChip(
                            onClick = { selectedCategory = cat },
                            label = { Text(label, fontSize = 11.sp) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (isSelected) IndiaGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = if (isSelected) IndiaGreen else MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }
        }

        // Meals List
        if (filteredMeals.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🍲", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isHindi) "कोई व्यंजन नहीं मिला" else "No meals found for this filter",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                items(filteredMeals) { meal ->
                    val quantity = cartItems.find { it.meal.id == meal.id }?.quantity ?: 0
                    MealCard(
                        meal = meal,
                        isHindi = isHindi,
                        quantityInCart = quantity,
                        onAddToCart = { onAddToCart(meal, "") },
                        onRemoveFromCart = { onRemoveFromCart(meal.id) },
                        onMealClick = {
                            selectedMealForDetail = meal
                            customNote = ""
                        }
                    )
                }
            }
        }

        // Meal Detail BottomSheet Modal
        selectedMealForDetail?.let { meal ->
            val quantity = cartItems.find { it.meal.id == meal.id }?.quantity ?: 0
            ModalBottomSheet(
                onDismissRequest = { selectedMealForDetail = null }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        AsyncImage(
                            model = meal.imageUrl,
                            contentDescription = meal.titleEn,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) meal.titleHi else meal.titleEn,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "₹${meal.price.toInt()}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = IndiaGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isHindi) meal.descriptionHi else meal.descriptionEn,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Included Items Checklist
                    Text(
                        text = if (isHindi) "थाली में शामिल आइटम:" else "Items Included in this Tiffin:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val itemsIncluded = if (isHindi) meal.itemsIncludedHi else meal.itemsIncludedEn
                    itemsIncluded.forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VegGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(item, fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Custom Instructions Text Box
                    OutlinedTextField(
                        value = customNote,
                        onValueChange = { customNote = it },
                        placeholder = { Text(if (isHindi) "कम मिर्च, एक्स्ट्रा रायता आदि लिखें..." else "Add special instructions e.g. Less spicy...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            onAddToCart(meal, customNote)
                            selectedMealForDetail = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(
                            text = if (isHindi) "कार्ट में जोड़ें (₹${meal.price.toInt()})" else "ADD TO CART (₹${meal.price.toInt()})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
