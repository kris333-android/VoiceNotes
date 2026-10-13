package com.voicenotes.app.core.database

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class VoiceNotesDatabase_Impl : VoiceNotesDatabase() {
  private val _noteDao: Lazy<NoteDao> = lazy {
    NoteDao_Impl(this)
  }

  private val _folderDao: Lazy<FolderDao> = lazy {
    FolderDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "2313ad453629af46d5d7ad27028ae4c1", "f8774b6262f46d875a3101c006ea094d") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `notes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `filePath` TEXT NOT NULL, `folderId` INTEGER, `durationMillis` INTEGER NOT NULL, `sizeBytes` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`folderId`) REFERENCES `folders`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_notes_folderId` ON `notes` (`folderId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `folders` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2313ad453629af46d5d7ad27028ae4c1')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `notes`")
        connection.execSQL("DROP TABLE IF EXISTS `folders`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsNotes: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsNotes.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNotes.put("title", TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNotes.put("filePath", TableInfo.Column("filePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNotes.put("folderId", TableInfo.Column("folderId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNotes.put("durationMillis", TableInfo.Column("durationMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNotes.put("sizeBytes", TableInfo.Column("sizeBytes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNotes.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysNotes: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysNotes.add(TableInfo.ForeignKey("folders", "SET NULL", "NO ACTION", listOf("folderId"), listOf("id")))
        val _indicesNotes: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesNotes.add(TableInfo.Index("index_notes_folderId", false, listOf("folderId"), listOf("ASC")))
        val _infoNotes: TableInfo = TableInfo("notes", _columnsNotes, _foreignKeysNotes, _indicesNotes)
        val _existingNotes: TableInfo = read(connection, "notes")
        if (!_infoNotes.equals(_existingNotes)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |notes(com.voicenotes.app.core.database.NoteEntity).
              | Expected:
              |""".trimMargin() + _infoNotes + """
              |
              | Found:
              |""".trimMargin() + _existingNotes)
        }
        val _columnsFolders: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsFolders.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFolders.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFolders.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysFolders: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesFolders: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoFolders: TableInfo = TableInfo("folders", _columnsFolders, _foreignKeysFolders, _indicesFolders)
        val _existingFolders: TableInfo = read(connection, "folders")
        if (!_infoFolders.equals(_existingFolders)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |folders(com.voicenotes.app.core.database.FolderEntity).
              | Expected:
              |""".trimMargin() + _infoFolders + """
              |
              | Found:
              |""".trimMargin() + _existingFolders)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "notes", "folders")
  }

  public override fun clearAllTables() {
    super.performClear(true, "notes", "folders")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(NoteDao::class, NoteDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(FolderDao::class, FolderDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun noteDao(): NoteDao = _noteDao.value

  public override fun folderDao(): FolderDao = _folderDao.value
}
