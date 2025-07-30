package com.denchic45.rickandmorty.ui.characters

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.denchic45.rickandmorty.data.api.model.CharacterGender
import com.denchic45.rickandmorty.data.api.model.CharacterStatus
import com.denchic45.rickandmorty.ui.HeaderItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersFilterBottomSheet(viewModel: CharactersViewModel, navigateBack: () -> Unit) {
    val state = viewModel.searchState
    ModalBottomSheet(
        onDismissRequest = {
            navigateBack()
            viewModel.onFiltersClose()
        },
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
    ) {
        HeaderItem("Status")
        Row(
            Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CharacterStatus.entries.forEach {
                StatusChip(
                    status = it,
                    selection = state.status,
                    onSelect = viewModel::onStatusFilterChange
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        HeaderItem("Gender")
        Row(
            Modifier
                .padding(horizontal = 16.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CharacterGender.entries.forEach {
                GenderChip(
                    gender = it,
                    selection = state.gender,
                    onSelect = viewModel::onGenderFilterChange
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Column(Modifier.padding(16.dp)) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.species,
                onValueChange = { viewModel.onSpeciesFilterChange(it) },
                label = { Text("Species") },
                trailingIcon = {
                    if (state.species.isNotEmpty()) IconButton({ viewModel.onSpeciesFilterChange("") }) {
                        Icon(Icons.Default.Cancel, null)
                    }
                }
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.type,
                onValueChange = { viewModel.onTypeFilterChange(it) },
                label = { Text("Type") },
                trailingIcon = {
                    if (state.type.isNotEmpty()) IconButton({ viewModel.onTypeFilterChange("") }) {
                        Icon(Icons.Default.Cancel, null)
                    }
                }
            )
            Spacer(Modifier.height(16.dp))
            Row {
                TextButton({
                    navigateBack()
                    viewModel.onFiltersClear()
                }, enabled = state.hasFilters, modifier = Modifier.weight(1f)) { Text("Clear") }
                Button({
                    navigateBack()
                    viewModel.onFiltersClose()
                }, Modifier.weight(1f)) { Text("Done") }
            }
        }
    }
}

@Composable
fun StatusChip(
    status: CharacterStatus,
    selection: CharacterStatus?,
    onSelect: (CharacterStatus) -> Unit
) {
    FilterChip(
        selected = selection == status,
        onClick = { onSelect(status) },
        label = { Text(status.displayName) }
    )
}

@Composable
fun GenderChip(
    gender: CharacterGender,
    selection: CharacterGender?,
    onSelect: (CharacterGender) -> Unit
) {
    FilterChip(
        selected = selection == gender,
        onClick = { onSelect(gender) },
        label = { Text(gender.displayName) }
    )
}