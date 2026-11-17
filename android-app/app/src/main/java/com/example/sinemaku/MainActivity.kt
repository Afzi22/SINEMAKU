
package com.example.sinemaku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ========================================
// WARNA TEMA SINEMAKU
// ========================================

private val Background = Color(0xFF10121B)
private val SurfaceDark = Color(0xFF232634)
private val AccentGold = Color(0xFFF4B44B)
private val MutedText = Color(0xFFA8AAB8)

// ========================================
// MODEL DATA FILM
// ========================================

data class Movie(
    val id: Int,
    val title: String,
    val genre: String,
    val year: Int,
    val rating: Double,
    val description: String,
    val posterColor: Color
)

// ========================================
// DATA FILM SINEMAKU
// ========================================

private val movies = listOf(
    Movie(
        id = 1,
        title = "Interstellar",
        genre = "Sci-Fi",
        year = 2014,
        rating = 8.7,
        description = "Perjalanan luar angkasa untuk mencari rumah baru umat manusia.",
        posterColor = Color(0xFF36516B)
    ),
    Movie(
        id = 2,
        title = "Inception",
        genre = "Thriller",
        year = 2010,
        rating = 8.8,
        description = "Misi memasuki mimpi untuk menanamkan sebuah ide.",
        posterColor = Color(0xFF40566A)
    ),
    Movie(
        id = 3,
        title = "The Dark Knight",
        genre = "Action",
        year = 2008,
        rating = 9.0,
        description = "Batman menghadapi ancaman Joker di Gotham City.",
        posterColor = Color(0xFF3B414C)
    ),
    Movie(
        id = 4,
        title = "Dune",
        genre = "Adventure",
        year = 2021,
        rating = 8.0,
        description = "Perjalanan Paul Atreides di planet Arrakis.",
        posterColor = Color(0xFF9A6745)
    ),
    Movie(
        id = 5,
        title = "The Chronicles of Narnia: Prince Caspian",
        genre = "Adventure",
        year = 2008,
        rating = 6.5,
        description = "Pevensie bersaudara kembali ke Narnia untuk membantu Pangeran Caspian merebut kembali takhtanya dari Raja Miraz.",
        posterColor = Color(0xFF356B58)
    )
)

// ========================================
// MAIN ACTIVITY
// ========================================

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = AccentGold,
                    background = Background,
                    surface = SurfaceDark
                )
            ) {
                SinemakuHomeScreen()
            }
        }
    }
}

// ========================================
// HOME SCREEN
// ========================================

@Composable
fun SinemakuHomeScreen() {

    var searchQuery by remember {
        mutableStateOf("")
    }

    var selectedGenre by remember {
        mutableStateOf("All")
    }

    val genres = listOf(
        "All",
        "Action",
        "Sci-Fi",
        "Thriller",
        "Adventure"
    )

    val filteredMovies = movies.filter { movie ->

        val matchesSearch =
            movie.title.contains(
                searchQuery,
                ignoreCase = true
            ) ||
                    movie.genre.contains(
                        searchQuery,
                        ignoreCase = true
                    )

        val matchesGenre =
            selectedGenre == "All" ||
                    movie.genre == selectedGenre

        matchesSearch && matchesGenre
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),

        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 24.dp,
            bottom = 32.dp
        ),

        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item {
            HeaderSection()
        }

        item {
            SearchSection(
                query = searchQuery,
                onQueryChange = {
                    searchQuery = it
                }
            )
        }

        item {
            FeaturedMovieSection(
                movie = movies.first()
            )
        }

        item {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Explore Genres",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                GenreSection(
                    genres = genres,
                    selectedGenre = selectedGenre,
                    onGenreSelected = {
                        selectedGenre = it
                    }
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Popular Movies",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${filteredMovies.size} movies",
                    color = MutedText,
                    fontSize = 13.sp
                )
            }
        }

        if (filteredMovies.isEmpty()) {

            item {
                Text(
                    text = "Film tidak ditemukan.",
                    color = MutedText,
                    modifier = Modifier.padding(
                        vertical = 24.dp
                    )
                )
            }

        } else {

            items(
                items = filteredMovies,
                key = { it.id }
            ) { movie ->

                ModernMovieCard(movie)
            }
        }
    }
}

// ========================================
// HEADER
// ========================================

@Composable
fun HeaderSection() {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(6.dp)
    ) {

        Text(
            text = "SINEMAKU",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AccentGold
        )

        Text(
            text = "Discover your next favorite movie",
            fontSize = 14.sp,
            color = MutedText
        )
    }
}

// ========================================
// SEARCH BAR
// ========================================

@Composable
fun SearchSection(
    query: String,
    onQueryChange: (String) -> Unit
) {

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),

        placeholder = {
            Text("Cari film atau genre...")
        },

        singleLine = true,
        shape = RoundedCornerShape(16.dp),

        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AccentGold,
            unfocusedBorderColor = Color(0xFF454957),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}

// ========================================
// FEATURED MOVIE
// ========================================

@Composable
fun FeaturedMovieSection(movie: Movie) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF32475E),
                        Color(0xFF151A27),
                        Background
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = "FEATURED MOVIE",
                color = AccentGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = movie.title,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "${movie.year} • ${movie.genre} • ★ ${movie.rating}",
                color = Color.LightGray,
                fontSize = 13.sp
            )

            Text(
                text = movie.description,
                color = Color.White,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ========================================
// FILTER GENRE
// ========================================

@Composable
fun GenreSection(
    genres: List<String>,
    selectedGenre: String,
    onGenreSelected: (String) -> Unit
) {

    LazyRow(
        horizontalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {

        items(genres) { genre ->

            val selected =
                genre == selectedGenre

            FilterChip(
                selected = selected,

                onClick = {
                    onGenreSelected(genre)
                },

                label = {
                    Text(genre)
                },

                colors =
                    FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentGold,
                        selectedLabelColor = Background,
                        containerColor = SurfaceDark,
                        labelColor = Color.White
                    )
            )
        }
    }
}

// ========================================
// MOVIE CARD
// ========================================

@Composable
fun ModernMovieCard(movie: Movie) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor = SurfaceDark
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),

            horizontalArrangement =
                Arrangement.spacedBy(16.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // Poster sementara berbentuk gradasi

            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                movie.posterColor,
                                Color(0xFF11141C)
                            )
                        )
                    ),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = movie.title.take(1),
                    color = Color.White,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Column(
                modifier = Modifier.weight(1f),

                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = movie.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${movie.year} • ${movie.genre}",
                    color = MutedText,
                    fontSize = 13.sp
                )

                Text(
                    text = "★ ${movie.rating}",
                    color = AccentGold,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = movie.description,
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
