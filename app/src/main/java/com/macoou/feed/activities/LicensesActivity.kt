package com.macoou.feed.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.compose.util.author
import com.mikepenz.aboutlibraries.util.withContext
import com.macoou.feed.activities.ui.theme.feedbrTheme
import com.macoou.feed.ui.TopBar
import kotlin.properties.Delegates

class LicensesActivity : ComponentActivity() {

    // SharedPreferences variables.
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sharedPrefEditor: SharedPreferences.Editor
    private lateinit var theme: String
    private lateinit var themeState: MutableState<String>
    private var materialYou by Delegates.notNull<Boolean>()
    private lateinit var materialYouState: MutableState<Boolean>

    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            themeState = remember { mutableStateOf(theme) }
            materialYouState = remember { mutableStateOf(materialYou) }
            feedbrTheme(
                theme = themeState.value,
                materialYou = materialYouState.value
            ) {
                UI()
            }
        }

        // Initialize late init variables.
        sharedPref = this.getSharedPreferences(
            SettingsActivity.PreferenceKeys.SETTINGS,
            Context.MODE_PRIVATE
        )
        sharedPrefEditor = sharedPref.edit()
        theme =
            sharedPref.getString(SettingsActivity.PreferenceKeys.THEME, "System Default").toString()
        materialYou = sharedPref.getBoolean(SettingsActivity.PreferenceKeys.MATERIAL_YOU, false)
    }


    @ExperimentalMaterial3Api
    @Composable
    fun UI() {
        // Set status bar and nav bar colours.
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = when (theme) {
            "Dark Theme" -> false
            "Light Theme" -> true
            else -> !isSystemInDarkTheme()
        }
        val color = MaterialTheme.colorScheme.background
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = color,
                darkIcons = useDarkIcons
            )
        }

        Surface(color = MaterialTheme.colorScheme.onBackground) {
            Scaffold(
                topBar = { TopBar("Open-Source Licenses") { finish() } },
                modifier = Modifier.background(MaterialTheme.colorScheme.onBackground)
            ) {
                // Call the library to generate the list of libraries
                LibrariesContainer(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    /**
     * Custom library class that now works with material3.
     */
    @Composable
    fun LibrariesContainer(
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(0.dp),
        showAuthor: Boolean = true,
        showVersion: Boolean = true,
        showLicenseBadges: Boolean = true
    ) {
        val libraries = remember { mutableStateOf<Libs?>(null) }
        val context = LocalContext.current
        LaunchedEffect(libraries) {
            libraries.value = Libs.Builder().withContext(context).build()
        }

        val libs = libraries.value?.libraries
        if (libs != null) {
            Libraries(
                libraries = libs,
                modifier,
                contentPadding,
                showAuthor,
                showVersion,
                showLicenseBadges
            )
        }
    }

    /**
     * Displays all provided libraries in a simple list.
     * Custom library class that now works with material3.
     */
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun Libraries(
        libraries: List<Library>,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(0.dp),
        showAuthor: Boolean = true,
        showVersion: Boolean = true,
        showLicenseBadges: Boolean = true,
    ) {
        LazyColumn(modifier, contentPadding = contentPadding) {
            items(libraries) { library ->
                val openDialog = remember { mutableStateOf(false) }

                Library(library, showAuthor, showVersion, showLicenseBadges) {
                    openDialog.value = true
                }

                if (openDialog.value) {
                    val scrollState = rememberScrollState()
                    Dialog(
                        onDismissRequest = {
                            openDialog.value = false
                        },
                        properties = DialogProperties(usePlatformDefaultWidth = false)
                    ) {
                        androidx.compose.material.Surface(
                            modifier = Modifier
                                .padding(8.dp)
                                .verticalScroll(scrollState)
                                .fillMaxSize()
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                androidx.compose.material.Text(
                                    text = library.licenses.firstOrNull()?.licenseContent ?: "",
                                )
                                androidx.compose.material.TextButton(
                                    onClick = { openDialog.value = false },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("OK")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Custom library class that now works with material3.
     */
    @Composable
    fun Library(
        library: Library,
        showAuthor: Boolean = true,
        showVersion: Boolean = true,
        showLicenseBadges: Boolean = true,
        onClick: () -> Unit
    ) {
        val typography = MaterialTheme.typography
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick.invoke()
                }
                .padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = library.name,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .weight(1f),
                    style = typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val version = library.artifactVersion
                if (version != null && showVersion) {
                    Text(
                        version,
                        modifier = Modifier.padding(start = 8.dp),
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
            }
            val author = library.author
            if (showAuthor && author.isNotBlank()) {
                androidx.compose.material.Text(
                    text = author,
                    style = typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            if (showLicenseBadges && library.licenses.isNotEmpty()) {
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    library.licenses.forEach {
                        Badge(
                            modifier = Modifier.padding(end = 4.dp),
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Text(text = it.name)
                        }
                    }
                }
            }
        }
    }
}