package com.faist.weatherapp.presentation.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.faist.weatherapp.R
import com.faist.weatherapp.domain.entity.City

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(component: SearchComponent) {

    val state by component.model.collectAsState()

    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    SearchBar(
        modifier = Modifier
            .focusRequester(focusRequester),
        inputField = {
            SearchBarDefaults.InputField(
                query = state.searchQuery,
                placeholder = { Text(text = stringResource(R.string.search)) },
                onQueryChange = { component.changeSearchQuery(it) },
                onSearch = { component.onClickSearch() },
                expanded = true,
                onExpandedChange = {},
                leadingIcon = {
                    IconButton(onClick = { component.onClickBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { component.onClickSearch() }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        expanded = true,
        onExpandedChange = {
            component.onClickBack()
        }
    ) {
        when (val searchState = state.searchState) {
            SearchStore.State.SearchState.EmptyResult -> {
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = stringResource(R.string.not_found)
                )
            }

            SearchStore.State.SearchState.Error -> {
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = stringResource(R.string.something_goes_wrong)
                )
            }

            SearchStore.State.SearchState.Initial -> {
            }

            SearchStore.State.SearchState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            is SearchStore.State.SearchState.SuccessLoaded -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surface),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(
                        items = searchState.cities,
                        key = { it.id }
                    ) { city ->
                        CityCard(city = city) {
                            component.onClickCity(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CityCard(
    city: City,
    onCityClick: (City) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable {
                    onCityClick(city)
                }
        ) {
            Text(
                text = city.name,
                fontStyle = MaterialTheme.typography.titleMedium.fontStyle
            )
            Spacer(modifier = Modifier)
            Text(
                text = city.country,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}































