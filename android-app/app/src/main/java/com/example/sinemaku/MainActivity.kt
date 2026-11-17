
package com.example.sinemaku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// =====================================
// WARNA APLIKASI
// =====================================

private val Background = Color(0xFF10121B)
private val SurfaceDark = Color(0xFF232634)
private val AccentGold = Color(0xFFF4B44B)
private val MutedText = Color(0xFFA8AAB8)

// =====================================
// MODEL FILM SEMENTARA
// =====================================

data class Movie(
    val id: Int,
    val title: String,
    val genre: String,
    val year: Int,
    val rating: Double,
    val description: String,
    val posterName: String,
    val posterColor: Color
)

// =====================================
// DATA FILM
// =====================================

private val movies = listOf(
    Movie(
        id = 1,
        title = "Interstellar",
        genre = "Sci-Fi",
        year = 2014,
        rating = 8.7,
        description = "Perjalanan luar angkasa untuk mencari rumah baru umat manusia.",
        posterName = "poster_interstellar",
        posterColor = Color(0xFF36516B)
    ),
    Movie(
        id = 2,
        title = "Inception",
        genre = "Thriller",
        year = 2010,
        rating = 8.8,
        description = "Misi memasuki mimpi untuk menanamkan sebuah ide.",
        posterName = "poster_inception",
        posterColor = Color(0xFF40566A)
    ),
    Movie(
        id = 3,
        title = "The Dark Knight",
        genre = "Action",
        year = 2008,
        rating = 9.0,
        description = "Batman menghadapi ancaman Joker di Gotham City.",
        posterName = "poster_dark_knight",
        posterColor = Color(0xFF3B414C)
    ),
    Movie(
        id = 4,
        title = "Dune",
        genre = "Adventure",
        year = 2021,
        rating = 8.0,
        description = "Perjalanan Paul Atreides di planet Arrakis.",
        posterName = "poster_dune",
        posterColor = Color(0xFF9A6745)
    ),
    Movie(
        id = 5,
        title = "The Chronicles of Narnia: Prince Caspian",
        genre = "Adventure",
        year = 2008,
        rating = 6.5,
        description = "Pevensie bersaudara kembali ke Narnia untuk membantu Pangeran Caspian.",
        posterName = "poster_narnia_prince_caspian",
        posterColor = Color(0xFF356B58)
    )
)

// =====================================
// MAIN ACTIVITY
// =====================================

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
                SinemakuApp()
            }
        }
    }
}

// =====================================
// NAVIGASI APLIKASI
// =====================================

@Composable
fun SinemakuApp() {

    // Simpan ID agar bisa dipulihkan setelah
    // rotasi layar atau pembuatan ulang Activity.
    var selectedMovieId by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    val selectedMovie = movies.firstOrNull {
        it.id == selectedMovieId
    }

    // Tangani tombol Back fisik/sistem Android
    // saat halaman detail sedang terbuka.
    BackHandler(enabled = selectedMovie != null) {
        selectedMovieId = null
    }

    // Kedua layar tetap berada di composition.
    // Home disembunyikan saat detail terbuka,
    // sehingga posisi scroll dan filter tetap ada.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        androidx.compose.runtime.CompositionLocalProvider(
            androidx.compose.ui.platform.LocalLayoutDirection provides
                    androidx.compose.ui.unit.LayoutDirection.Ltr
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                // Home tetap dipertahankan di composition.
                // Saat detail terbuka, Home tidak menerima
                // input karena detail menutup seluruh layar.
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    SinemakuHomeScreen(
                        onMovieClick = { movie ->
                            selectedMovieId = movie.id
                        }
                    )
                }

                if (selectedMovie != null) {
                    MovieDetailScreen(
                        movie = selectedMovie,
                        onBackClick = {
                            selectedMovieId = null
                        }
                    )
                }
            }
        }
    }
}

// =====================================
// HOME SCREEN
// =====================================

@Composable
fun SinemakuHomeScreen(
    onMovieClick: (Movie) -> Unit
) {

    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    var selectedGenre by rememberSaveable {
        mutableStateOf("All")
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

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

    val showBackToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 ||
                    listState.firstVisibleItemScrollOffset > 250
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            userScrollEnabled = true,
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 24.dp,
                bottom = 90.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item(key = "header") {
                HeaderSection()
            }

            item(key = "search") {
                SearchSection(
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                    }
                )
            }

            item(key = "featured") {
                FeaturedMovieSection(
                    movie = movies.first(),
                    onClick = {
                        onMovieClick(movies.first())
                    }
                )
            }

            item(key = "genres") {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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

            item(key = "popular_title") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
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
                item(key = "empty") {
                    Text(
                        text = "Film tidak ditemukan.",
                        color = MutedText,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            } else {
                items(
                    items = filteredMovies,
                    key = { movie -> "movie_${movie.id}" }
                ) { movie ->
                    ModernMovieCard(
                        movie = movie,
                        onClick = {
                            onMovieClick(movie)
                        }
                    )
                }
            }

            item(key = "scroll_end") {
                Text(
                    text = "You've reached the end",
                    color = MutedText,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }

        if (showBackToTop) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
                containerColor = AccentGold,
                contentColor = Background
            ) {
                Text(
                    text = "↑",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// =====================================
// HEADER
// =====================================

@Composable
fun HeaderSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
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

// =====================================
// SEARCH
// =====================================

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
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AccentGold,
            unfocusedBorderColor = Color(0xFF454957),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}

// =====================================
// FEATURED MOVIE
// =====================================

@Composable
fun FeaturedMovieSection(
    movie: Movie,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceDark)
            .clickable(onClick = onClick)
    ) {
        MoviePoster(
            movie = movie,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.12f),
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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

// =====================================
// GENRE FILTER
// =====================================

@Composable
fun GenreSection(
    genres: List<String>,
    selectedGenre: String,
    onGenreSelected: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(genres) { genre ->
            FilterChip(
                selected = genre == selectedGenre,
                onClick = {
                    onGenreSelected(genre)
                },
                label = {
                    Text(genre)
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AccentGold,
                    selectedLabelColor = Background,
                    containerColor = SurfaceDark,
                    labelColor = Color.White
                )
            )
        }
    }
}

// =====================================
// POSTER FILM
// =====================================

@Composable
fun MoviePoster(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val posterRes = remember(movie.posterName, context) {
        context.resources.getIdentifier(
            movie.posterName,
            "drawable",
            context.packageName
        )
    }

    if (posterRes != 0) {
        Image(
            painter = painterResource(id = posterRes),
            contentDescription = "Poster ${movie.title}",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = modifier.background(
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
    }
}

// =====================================
// KARTU FILM (BISA DIKLIK)
// =====================================

@Composable
fun ModernMovieCard(
    movie: Movie,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
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
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MoviePoster(
                movie = movie,
                modifier = Modifier
                    .width(100.dp)
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
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

// =====================================
// HALAMAN DETAIL FILM
// =====================================

@Composable
fun MovieDetailScreen(
    movie: Movie,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        // Header tetap terlihat saat isi detail di-scroll.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = onBackClick) {
                Text(
                    text = "← Back",
                    color = AccentGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Text(
                text = "Movie Details",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = true,
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                bottom = 36.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                MoviePoster(
                    movie = movie,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = movie.title,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "${movie.year} • ${movie.genre}",
                        color = MutedText,
                        fontSize = 15.sp
                    )

                    Text(
                        text = "★ ${movie.rating} / 10",
                        color = AccentGold,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                HorizontalDivider(
                    color = Color(0xFF353947)
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Synopsis",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = movie.description,
                        color = Color.LightGray,
                        fontSize = 15.sp,
                        lineHeight = 24.sp
                    )
                }
            }
        }
    }
}
