package com.savlukov.app.presentation.screens.loyalty

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.savlukov.app.presentation.common.SavlukovCard
import com.savlukov.app.presentation.components.loyalty.CircleOfCraft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoyaltyScreen() {
    var winMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "THE CIRCLE OF CRAFT",
                        style = MaterialTheme.typography.titleMedium,
                        letterSpacing = 2.sp
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(24.dp)
        ) {
            item {
                Text(
                    text = "Unlock Exclusive Experiences",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                CircleOfCraft(
                    rewards = listOf(
                        "Bespoke Consultation",
                        "Material Kit",
                        "Heritage Tour",
                        "Early Access",
                        "Designer Sketch",
                        "Craftsman's Gift"
                    ),
                    onWin = { winMessage = it }
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                if (winMessage != null) {
                    Text(
                        text = "You've unlocked: $winMessage",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = "Recent Rewards",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            items(listOf(
                "Elena from Minsk won a Bespoke Consultation.",
                "Dmitry from Gomel received a Material Kit.",
                "Anna from Brest unlocked the Heritage Tour."
            )) { winner ->
                SavlukovCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = winner,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
