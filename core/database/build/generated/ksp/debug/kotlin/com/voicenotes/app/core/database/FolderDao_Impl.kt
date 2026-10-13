package com.voicenotes.app.core.database

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class FolderDao_Impl(
  __db: RoomDatabase,
) : FolderDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfFolderEntity: EntityInsertAdapter<FolderEntity>

  private val __deleteAdapterOfFolderEntity: EntityDeleteOrUpdateAdapter<FolderEntity>

  private val __updateAdapterOfFolderEntity: EntityDeleteOrUpdateAdapter<FolderEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfFolderEntity = object : EntityInsertAdapter<FolderEntity>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `folders` (`id`,`name`,`createdAt`) VALUES (nullif(?, 0),?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: FolderEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindLong(3, entity.createdAt)
      }
    }
    this.__deleteAdapterOfFolderEntity = object : EntityDeleteOrUpdateAdapter<FolderEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `folders` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: FolderEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfFolderEntity = object : EntityDeleteOrUpdateAdapter<FolderEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `folders` SET `id` = ?,`name` = ?,`createdAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: FolderEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindLong(3, entity.createdAt)
        statement.bindLong(4, entity.id)
      }
    }
  }

  public override suspend fun insert(folder: FolderEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfFolderEntity.insertAndReturnId(_connection, folder)
    _result
  }

  public override suspend fun delete(folder: FolderEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfFolderEntity.handle(_connection, folder)
  }

  public override suspend fun update(folder: FolderEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfFolderEntity.handle(_connection, folder)
  }

  public override fun observeFoldersWithCounts(): Flow<List<FolderWithCountRow>> {
    val _sql: String = """
        |
        |        SELECT folders.*, COUNT(notes.id) AS noteCount
        |        FROM folders
        |        LEFT JOIN notes ON notes.folderId = folders.id
        |        GROUP BY folders.id
        |        ORDER BY folders.createdAt DESC
        |        
        """.trimMargin()
    return createFlow(__db, false, arrayOf("folders", "notes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfNoteCount: Int = getColumnIndexOrThrow(_stmt, "noteCount")
        val _result: MutableList<FolderWithCountRow> = mutableListOf()
        while (_stmt.step()) {
          val _item: FolderWithCountRow
          val _tmpNoteCount: Int
          _tmpNoteCount = _stmt.getLong(_columnIndexOfNoteCount).toInt()
          val _tmpFolder: FolderEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _tmpFolder = FolderEntity(_tmpId,_tmpName,_tmpCreatedAt)
          _item = FolderWithCountRow(_tmpFolder,_tmpNoteCount)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getFolderById(folderId: Long): FolderEntity? {
    val _sql: String = "SELECT * FROM folders WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, folderId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: FolderEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _result = FolderEntity(_tmpId,_tmpName,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeAllNotesCount(): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM notes"
    return createFlow(__db, false, arrayOf("notes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeUnfiledNotesCount(): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM notes WHERE folderId IS NULL"
    return createFlow(__db, false, arrayOf("notes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
