package com.example.sinemaku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sinemaku.ui.model.TayanganSampleData
import com.example.sinemaku.ui.model.TayanganUiModel

private val Bg = Color(0xFF101116)
private val Panel = Color(0xFF1B1D27)
private val Gold = Color(0xFFF4B44B)
private val Muted = Color(0xFFA7A9B4)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SinemakuTheme { SinemakuApp() } }
    }
}

@Composable
private fun SinemakuTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Gold,
            onPrimary = Bg,
            background = Bg,
            onBackground = Color.White,
            surface = Panel,
            onSurface = Color.White
        ),
        content = content
    )
}

private enum class Tab(val label: String) {
    HOME("Beranda"), BROWSE("Jelajahi"), WATCHLIST("Watchlist"), HISTORY("Riwayat"), MORE("Lainnya")
}

@Composable
private fun SinemakuApp() {
    val movies = remember { TayanganSampleData.items }
    var tab by rememberSaveable { mutableStateOf(Tab.HOME) }
    var selectedId by rememberSaveable { mutableStateOf<String?>(null) }
    var playingId by rememberSaveable { mutableStateOf<String?>(null) }
    val watchlist = remember { mutableStateListOf<String>() }
    val history = remember { mutableStateListOf<String>() }
    val selectedMovie = movies.firstOrNull { it.id == selectedId }
    val playingMovie = movies.firstOrNull { it.id == playingId }
    val playingTrailer = playingMovie?.let { TrailerRepository.getTrailer(it.title) }

    BackHandler(enabled = selectedId != null && playingId == null) {
        selectedId = null
    }

    // Satu halaman aktif dalam satu waktu: player, detail, atau tab utama.
    if (playingMovie != null && playingTrailer != null) {
        VideoPlayerScreen(
            title = playingMovie.title,
            videoResId = playingTrailer,
            onBack = { playingId = null }
        )
    } else {
        Scaffold(
            containerColor = Bg,
            bottomBar = {
                if (selectedMovie == null) {
                    NavigationBar(containerColor = Panel, tonalElevation = 0.dp) {
                        Tab.entries.forEach { item ->
                            NavigationBarItem(
                                selected = tab == item,
                                onClick = { tab = item },
                                icon = { Icon(tabIcon(item), contentDescription = item.label) },
                                label = { Text(item.label, fontSize = 10.sp, maxLines = 1) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Gold,
                                    selectedTextColor = Gold,
                                    indicatorColor = Color(0xFF34303A),
                                    unselectedIconColor = Muted,
                                    unselectedTextColor = Muted
                                )
                            )
                        }
                    }
                }
            }
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                if (selectedMovie != null) {
                    DetailScreen(
                        movie = selectedMovie,
                        saved = selectedMovie.id in watchlist,
                        onBack = { selectedId = null },
                        onSave = { toggle(watchlist, selectedMovie.id) },
                        onWatch = {
                            if (TrailerRepository.getTrailer(selectedMovie.title) != null) {
                                history.remove(selectedMovie.id)
                                history.add(selectedMovie.id)
                                playingId = selectedMovie.id
                                true
                            } else {
                                false
                            }
                        }
                    )
                } else {
                    when (tab) {
                        Tab.HOME -> HomeScreen(movies, { selectedId = it }, watchlist, { toggle(watchlist, it) })
                        Tab.BROWSE -> BrowseScreen(movies, { selectedId = it }, watchlist, { toggle(watchlist, it) })
                        Tab.WATCHLIST -> CollectionScreen(
                            title = "Watchlist Saya", subtitle = "Tayangan yang ingin kamu tonton nanti",
                            movies = movies.filter { it.id in watchlist }, empty = "Belum ada tayangan di Watchlist",
                            onOpen = { selectedId = it }, onRemove = { watchlist.remove(it) }
                        )
                        Tab.HISTORY -> CollectionScreen(
                            title = "Riwayat Trailer", subtitle = "Trailer yang pernah kamu buka",
                            movies = history.asReversed().mapNotNull { id -> movies.firstOrNull { it.id == id } },
                            empty = "Belum ada riwayat trailer", onOpen = { selectedId = it },
                            onRemove = { history.remove(it) }
                        )
                        Tab.MORE -> MoreScreen()
                    }
                }
            }
        }
    }
}

private fun toggle(list: MutableList<String>, id: String) {
    if (id in list) list.remove(id) else list.add(id)
}

private fun tabIcon(tab: Tab) = when (tab) {
    Tab.HOME -> Icons.Default.Home
    Tab.BROWSE -> Icons.Default.Explore
    Tab.WATCHLIST -> Icons.Default.Bookmark
    Tab.HISTORY -> Icons.Default.History
    Tab.MORE -> Icons.Default.MoreHoriz
}

@Composable
private fun Header(subtitle: String = "Temukan tontonan favoritmu") {
    Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text("SINEMAKU", color = Gold, fontWeight = FontWeight.ExtraBold, fontSize = 25.sp)
            Text(subtitle, color = Muted, fontSize = 12.sp)
        }
        Icon(Icons.Default.Movie, contentDescription = null, tint = Gold)
    }
}

@Composable
private fun Poster(movie: TayanganUiModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val resource = remember(movie.posterName) {
        context.resources.getIdentifier(movie.posterName, "drawable", context.packageName)
    }
    Box(modifier.clip(RoundedCornerShape(14.dp)).background(
        Brush.verticalGradient(listOf(Color(0xFF434054), Color(0xFF171820)))
    ), contentAlignment = Alignment.Center) {
        if (resource != 0) {
            Image(
                painter = painterResource(resource), contentDescription = "Poster ${movie.title}",
                modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
            )
        } else {
            Text(movie.title.take(1), fontSize = 52.sp, color = Gold, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun HomeScreen(
    movies: List<TayanganUiModel>, onOpen: (String) -> Unit,
    watchlist: List<String>, onToggle: (String) -> Unit
) {
    val state = rememberLazyListState()
    LazyColumn(state = state, modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 28.dp)) {
        item { Header() }
        if (movies.isNotEmpty()) {
            item {
                val featured = movies.first()
                Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(250.dp).clickable { onOpen(featured.id) }) {
                    Poster(featured, Modifier.fillMaxSize())
                    Box(Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)).background(
                        Brush.verticalGradient(listOf(Color.Transparent, Bg.copy(alpha = 0.94f)))
                    ))
                    Column(Modifier.align(Alignment.BottomStart).padding(18.dp)) {
                        Text("PILIHAN SINEMAKU", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(featured.title, fontSize = 25.sp, fontWeight = FontWeight.Bold, maxLines = 2)
                        Text("${featured.year}  •  ${featured.genreText}  •  ★ ${featured.rating}", color = Color.White, fontSize = 12.sp)
                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(onClick = { onOpen(featured.id) }) {
                                Icon(Icons.Default.PlayArrow, null)
                                Text("Lihat Detail")
                            }
                            OutlinedButton(onClick = { onToggle(featured.id) }) {
                                Icon(if (featured.id in watchlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder, null)
                            }
                        }
                    }
                }
            }
        }
        item { SectionTitle("Sedang Tren") }
        item {
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(movies, key = { it.id }) { movie ->
                    PosterCard(movie, Modifier.width(145.dp), onOpen)
                }
            }
        }
        item { SectionTitle("Serial TV Pilihan") }
        item {
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(movies.filter { it.category == "Serial TV" }, key = { it.id }) { movie ->
                    PosterCard(movie, Modifier.width(145.dp), onOpen)
                }
            }
        }
        item { SectionTitle("Dokumenter Pilihan") }
        item {
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(movies.filter { it.category == "Dokumenter" }, key = { it.id }) { movie ->
                    PosterCard(movie, Modifier.width(145.dp), onOpen)
                }
            }
        }
        item { SectionTitle("Rekomendasi untuk Kamu") }
        item {
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(movies.reversed(), key = { it.id }) { movie ->
                    PosterCard(movie, Modifier.width(145.dp), onOpen)
                }
            }
        }
        item { SectionTitle("Jelajahi Genre") }
        item {
            val genres = movies.flatMap { it.genres }.distinct()
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(genres) { genre ->
                    Surface(color = Panel, shape = RoundedCornerShape(50)) {
                        Text(genre, modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp), color = Gold)
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(title, Modifier.padding(start = 18.dp, end = 18.dp, top = 24.dp, bottom = 12.dp), fontSize = 19.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun PosterCard(movie: TayanganUiModel, modifier: Modifier = Modifier, onOpen: (String) -> Unit) {
    Column(modifier.clickable { onOpen(movie.id) }) {
        Box {
            Poster(movie, Modifier.fillMaxWidth().height(205.dp))
            Surface(Modifier.align(Alignment.BottomEnd).padding(6.dp), color = Bg.copy(alpha = 0.85f), shape = RoundedCornerShape(8.dp)) {
                Text("★ ${movie.rating}", Modifier.padding(horizontal = 7.dp, vertical = 4.dp), color = Gold, fontSize = 12.sp)
            }
        }
        Spacer(Modifier.height(7.dp))
        Text(movie.title, maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.SemiBold)
        Text("${movie.category} • ${movie.year}", color = Muted, fontSize = 11.sp)
    }
}

@Composable
private fun BrowseScreen(
    movies: List<TayanganUiModel>, onOpen: (String) -> Unit,
    watchlist: List<String>, onToggle: (String) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("Semua") }
    var genre by rememberSaveable { mutableStateOf("Semua Genre") }
    val genres = remember(movies) { listOf("Semua Genre") + movies.flatMap { it.genres }.distinct().sorted() }
    val filtered = movies.filter { movie ->
        (category == "Semua" || movie.category == category) &&
                (genre == "Semua Genre" || movie.genres.any { it.equals(genre, true) }) &&
                (query.isBlank() || movie.title.contains(query.trim(), ignoreCase = true))
    }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 28.dp)) {
        item { Header("Film, Serial TV, dan Dokumenter") }
        item {
            OutlinedTextField(
                value = query, onValueChange = { query = it },
                label = { Text("Cari judul tayangan") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                singleLine = true, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }
        item {
            LazyRow(contentPadding = PaddingValues(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listOf("Semua", "Film", "Serial TV", "Dokumenter")) { item ->
                    FilterChip(selected = category == item, onClick = { category = item }, label = { Text(item) })
                }
            }
        }
        item {
            Text("Genre", Modifier.padding(horizontal = 18.dp), fontWeight = FontWeight.SemiBold)
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(genres) { item ->
                    FilterChip(selected = genre == item, onClick = { genre = item }, label = { Text(item) })
                }
            }
        }
        if (filtered.isEmpty()) {
            item {
                EmptyState(if (category == "Serial TV" || category == "Dokumenter")
                    "Belum ada $category yang cocok" else "Tidak ada tayangan yang cocok")
            }
        } else {
            items(filtered, key = { it.id }) { movie ->
                Row(Modifier.fillMaxWidth().clickable { onOpen(movie.id) }.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Poster(movie, Modifier.width(95.dp).height(125.dp))
                    Column(Modifier.weight(1f).padding(start = 14.dp)) {
                        Text(movie.title, fontWeight = FontWeight.Bold, maxLines = 2)
                        Text("${movie.category} • ${movie.year} • ${movie.genreText}", color = Muted, fontSize = 12.sp)
                        Text("★ ${movie.rating} • ${movie.durationText}", color = Gold, fontSize = 12.sp)
                    }
                    IconButton(onClick = { onToggle(movie.id) }) {
                        Icon(if (movie.id in watchlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Ubah watchlist", tint = Gold)
                    }
                }
            }
        }
    }
}

@Composable
private fun CollectionScreen(
    title: String, subtitle: String, movies: List<TayanganUiModel>, empty: String,
    onOpen: (String) -> Unit, onRemove: (String) -> Unit
) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 24.dp)) {
        item {
            Column(Modifier.padding(20.dp)) {
                Text(title, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = Muted, fontSize = 13.sp)
            }
        }
        if (movies.isEmpty()) item { EmptyState(empty) }
        items(movies, key = { it.id }) { movie ->
            Row(Modifier.fillMaxWidth().clickable { onOpen(movie.id) }.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Poster(movie, Modifier.size(width = 88.dp, height = 116.dp))
                Column(Modifier.weight(1f).padding(start = 14.dp)) {
                    Text(movie.title, fontWeight = FontWeight.SemiBold, maxLines = 2)
                    Text("${movie.category} • ${movie.year} • ${movie.genreText}", color = Muted, fontSize = 12.sp)
                }
                IconButton(onClick = { onRemove(movie.id) }) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Hapus dari $title", tint = Muted)
                }
            }
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Column(Modifier.fillMaxWidth().padding(36.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.MovieFilter, null, tint = Muted, modifier = Modifier.size(42.dp))
        Spacer(Modifier.height(12.dp))
        Text(message, color = Muted)
    }
}

@Composable
private fun MoreScreen() {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Lainnya", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Text("Fitur pendukung SINEMAKU", color = Muted)
        Spacer(Modifier.height(20.dp))
        listOf("Unduhan Offline (Sprint berikutnya)", "Profil Pengguna (segera)", "Pengaturan (segera)", "Tentang SINEMAKU").forEach { title ->
            Row(Modifier.fillMaxWidth().padding(vertical = 17.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(title, Modifier.weight(1f))
                Icon(Icons.Default.ChevronRight, null, tint = Muted)
            }
            HorizontalDivider(color = Color(0xFF343540))
        }
    }
}

@Composable
private fun DetailScreen(
    movie: TayanganUiModel, saved: Boolean, onBack: () -> Unit,
    onSave: () -> Unit, onWatch: () -> Boolean
) {
    var notice by remember(movie.id) { mutableStateOf(false) }
    LazyColumn(Modifier.fillMaxSize().background(Bg), contentPadding = PaddingValues(bottom = 32.dp)) {
        item {
            Box(Modifier.fillMaxWidth().height(340.dp)) {
                Poster(movie, Modifier.fillMaxSize())
                Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Bg.copy(alpha = 0.15f), Bg))))
                IconButton(onClick = onBack, modifier = Modifier.align(Alignment.TopStart).padding(12.dp).background(Panel, RoundedCornerShape(50))) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                }
            }
        }
        item {
            Column(Modifier.padding(horizontal = 20.dp)) {
                Text(movie.title, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(8.dp))
                Text("${movie.category} • ${movie.year} • ${movie.genreText}", color = Muted)
                Text("★ ${movie.rating} / 10  •  ${movie.durationText}", color = Gold, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                Text(movie.creatorLabel, color = Muted, fontSize = 13.sp)
                Spacer(Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = { notice = !onWatch() }, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.PlayArrow, null)
                        Spacer(Modifier.width(6.dp))
                        Text("Tonton Trailer")
                    }
                    OutlinedButton(onClick = onSave) {
                        Icon(if (saved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder, null)
                        Spacer(Modifier.width(5.dp))
                        Text(if (saved) "Tersimpan" else "Watchlist")
                    }
                }
                if (notice) {
                    Spacer(Modifier.height(10.dp))
                    Text("Trailer belum tersedia untuk tayangan ini.", color = Gold, fontSize = 12.sp)
                }
                Spacer(Modifier.height(24.dp))
                Text("Sinopsis", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(movie.synopsis, color = Color(0xFFDBDCE3), lineHeight = 23.sp)
            }
        }
    }
}
