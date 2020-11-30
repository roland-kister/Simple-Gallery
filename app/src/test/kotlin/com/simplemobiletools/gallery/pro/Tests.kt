package com.simplemobiletools.gallery.pro

import android.content.Context
import com.bumptech.glide.load.Options
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.Before
import com.simplemobiletools.gallery.pro.extensions.*;
import com.simplemobiletools.gallery.pro.helpers.*
import com.simplemobiletools.gallery.pro.models.Medium
import com.simplemobiletools.gallery.pro.svg.SvgDecoder
import java.io.FileInputStream
import java.io.InputStream
import java.util.ArrayList
import com.simplemobiletools.commons.helpers.SORT_DESCENDING

class Tests {
    private val excludedPaths = HashSet<String>()
    private val includedPaths = HashSet<String>()
    private val folderNoMediaStatuses = HashMap<String, Boolean>()
    private val noMediaFolders = ArrayList<String>()

    @Before
    fun init() {
        excludedPaths.add("excluded")
        excludedPaths.add("test/excluded")

        includedPaths.add("test")
        includedPaths.add("gallery")

        folderNoMediaStatuses["test/nomedia"] = false
        folderNoMediaStatuses["test/media"] = true

        noMediaFolders.add("empty")
        noMediaFolders.add("test/empty")
    }

    @Test
        fun isThisOrParentIncludedTest() {
        assertEquals(".hidden".isThisOrParentIncluded(includedPaths), false)
        assertEquals("excluded".isThisOrParentIncluded(includedPaths), false)
        assertEquals("excluded/test".isThisOrParentIncluded(includedPaths), false)
        assertEquals("excluded/test/folder".isThisOrParentIncluded(includedPaths), false)
        assertEquals("test/excluded".isThisOrParentIncluded(includedPaths), true)
        assertEquals("test".isThisOrParentIncluded(includedPaths), true)
        assertEquals("gallery".isThisOrParentIncluded(includedPaths), true)
        assertEquals("test/nomedia".isThisOrParentIncluded(includedPaths), true)
        assertEquals("test/media".isThisOrParentIncluded(includedPaths), true)
        assertEquals("empty".isThisOrParentIncluded(includedPaths), false)
        assertEquals("test/empty".isThisOrParentIncluded(includedPaths), true)
    }

    @Test
    fun isThisOrParentExcludedTest() {
        assertEquals(".hidden".isThisOrParentExcluded(excludedPaths), false)
        assertEquals("excluded".isThisOrParentExcluded(excludedPaths), true)
        assertEquals("excluded/test".isThisOrParentExcluded(excludedPaths), true)
        assertEquals("excluded/test/folder".isThisOrParentExcluded(excludedPaths), true)
        assertEquals("test/excluded".isThisOrParentExcluded(excludedPaths), true)
        assertEquals("test".isThisOrParentExcluded(excludedPaths), false)
        assertEquals("gallery".isThisOrParentExcluded(excludedPaths), false)
        assertEquals("test/nomedia".isThisOrParentExcluded(excludedPaths), false)
        assertEquals("test/media".isThisOrParentExcluded(excludedPaths), false)
        assertEquals("empty".isThisOrParentExcluded(excludedPaths), false)
        assertEquals("test/empty".isThisOrParentExcluded(excludedPaths), false)
    }

    @Test
    fun shouldFolderBeVisibleTest() {
        var showHidden = true

        val folderNomediaStatuses = HashMap<String, Boolean>()

        val callback = { path: String, hasNoMedia: Boolean ->
            folderNomediaStatuses[path] = hasNoMedia
        }

        assertEquals(".hidden".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), true)
        assertEquals("excluded".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), false)
        assertEquals("excluded/test".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), false)
        assertEquals("excluded/test/folder".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), false)
        assertEquals("test/excluded".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), false)
        assertEquals("test".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), true)
        assertEquals("gallery".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), true)
        assertEquals("test/nomedia".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), true)
        assertEquals("test/media".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), true)
        assertEquals("empty".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), true)
        assertEquals("test/empty".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), true)

        showHidden = false

        assertEquals(".hidden".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), false)
        assertEquals("excluded".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), false)
        assertEquals("excluded/test".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), false)
        assertEquals("excluded/test/folder".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), false)
        assertEquals("test/excluded".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), false)
        assertEquals("test".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), true)
        assertEquals("gallery".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), true)
        assertEquals("test/nomedia".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), true)
        assertEquals("test/media".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), true)
        assertEquals("empty".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), false)
        assertEquals("test/empty".shouldFolderBeVisible(excludedPaths, includedPaths, showHidden, folderNoMediaStatuses, noMediaFolders, callback), false)

    }

    @Test
    fun getDirMediaTypesTest() {
        var arrayList = ArrayList<Medium>()
        arrayList.add(Medium(1, "test.jpg", "/pictures/test.jpg", "/pictures/", 1, 1, 1, TYPE_IMAGES, 0, false, 0))
        assertEquals(arrayList.getDirMediaTypes(), TYPE_IMAGES)

        arrayList = ArrayList<Medium>()
        arrayList.add(Medium(1, "test.jpg", "/pictures/test.jpg", "/pictures/", 1, 1, 1, TYPE_IMAGES, 0, false, 0))
        arrayList.add(Medium(1, "test2.jpg", "/pictures/test2.jpg", "/pictures/", 1, 1, 1, TYPE_IMAGES, 0, false, 0))
        arrayList.add(Medium(1, "test.jpg", "/pictures/test.jpg", "/pictures/", 1, 1, 1, 0, 0, false, 0))
        assertEquals(arrayList.getDirMediaTypes(), TYPE_IMAGES)

        arrayList = ArrayList<Medium>()
        arrayList.add(Medium(1, "test.raw", "/pictures/test.raw", "/pictures/", 1, 1, 1, TYPE_RAWS, 0, false, 0))
        arrayList.add(Medium(1, "portrait.raw", "/pictures/portrait.raw", "/pictures/", 1, 1, 1, TYPE_PORTRAITS, 0, false, 0))
        arrayList.add(Medium(1, "funny.gif", "/pictures/funny.gif", "/pictures/", 1, 1, 1, TYPE_GIFS, 0, false, 0))
        arrayList.add(Medium(1, "logo.svg", "/pictures/logo.svg", "/pictures/", 1, 1, 1, TYPE_SVGS, 0, false, 0))
        arrayList.add(Medium(1, "meme.mp4", "/pictures/meme.mp4", "/pictures/", 1, 1, 1, TYPE_VIDEOS, 0, false, 0))
        assertEquals(arrayList.getDirMediaTypes(), TYPE_VIDEOS + TYPE_SVGS + TYPE_GIFS + TYPE_PORTRAITS + TYPE_RAWS)
    }

    @Test
    fun isSortingAscendingTest() {
        assertEquals(1.isSortingAscending(), true);
        assertEquals(0.isSortingAscending(), true);
        assertEquals((-1).isSortingAscending(), true);
        assertEquals(2.isSortingAscending(), true);
        assertEquals(1024.isSortingAscending(), false);
        assertEquals(SORT_DESCENDING.isSortingAscending(), false);
    }
}
