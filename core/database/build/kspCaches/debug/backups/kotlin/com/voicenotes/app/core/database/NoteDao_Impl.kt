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
public class NoteDao_Impl(
  __db: RoomDatabase,
) : NoteDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfNoteEntity: EntityInsertAdapter<NoteEntity>

  private val __deleteAdapterOfNoteEntity: EntityDeleteOrUpdateAdapter<NoteEntity>

  private val __updateAdapterOfNoteEntity: EntityDeleteOrUpdateAdapter<NoteEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfNoteEntity = object : EntityInsertAdapter<NoteEntity>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `notes` (`id`,`title`,`filePath`,`folderId`,`durationMillis`,`sizeBytes`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: NoteEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.filePath)
        val _tmpFolderId: Long? = entity.folderId
        if (_tmpFolderId == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmpFolderId)
        }
        statement.bindLong(5, entity.durationMillis)
        statement.bindLong(6, entity.sizeBytes)
        statement.bindLong(7, entity.createdAt)
      }
    }
    this.__deleteAdapterOfNoteEntity = object : EntityDeleteOrUpdateAdapter<NoteEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `notes` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: NoteEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfNoteEntity = object : EntityDeleteOrUpdateAdapter<NoteEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `notes` SET `id` = ?,`title` = ?,`filePath` = ?,`folderId` = ?,`durationMillis` = ?,`sizeBytes` = ?,`createdAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: NoteEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.filePath)
        val _tmpFolderId: Long? = entity.folderId
        if (_tmpFolderId == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmpFolderId)
        }
        statement.bindLong(5, entity.durationMillis)
        statement.bindLong(6, entity.sizeBytes)
        statement.bindLong(7, entity.createdAt)
        statement.bindLong(8, entity.id)
      }
    }
  }

  public override suspend fun insert(note: NoteEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfNoteEntity.insertAndReturnId(_connection, note)
    _result
  }

  public override suspend fun delete(note: NoteEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfNoteEntity.handle(_connection, note)
  }

  public override suspend fun update(note: NoteEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfNoteEntity.handle(_connection, note)
  }

  public override fun observeAllNotes(): Flow<List<NoteEntity>> {
    val _sql: String = "SELECT * FROM notes ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("notes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfFilePath: Int = getColumnIndexOrThrow(_stmt, "filePath")
        val _columnIndexOfFolderId: Int = getColumnIndexOrThrow(_stmt, "folderId")
        val _columnIndexOfDurationMillis: Int = getColumnIndexOrThrow(_stmt, "durationMillis")
        val _columnIndexOfSizeBytes: Int = getColumnIndexOrThrow(_stmt, "sizeBytes")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<NoteEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NoteEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpFilePath: String
          _tmpFilePath = _stmt.getText(_columnIndexOfFilePath)
          val _tmpFolderId: Long?
          if (_stmt.isNull(_columnIndexOfFolderId)) {
            _tmpFolderId = null
          } else {
            _tmpFolderId = _stmt.getLong(_columnIndexOfFolderId)
          }
          val _tmpDurationMillis: Long
          _tmpDurationMillis = _stmt.getLong(_columnIndexOfDurationMillis)
          val _tmpSizeBytes: Long
          _tmpSizeBytes = _stmt.getLong(_columnIndexOfSizeBytes)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item = NoteEntity(_tmpId,_tmpTitle,_tmpFilePath,_tmpFolderId,_tmpDurationMillis,_tmpSizeBytes,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeUnfiledNotes(): Flow<List<NoteEntity>> {
    val _sql: String = "SELECT * FROM notes WHERE folderId IS NULL ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("notes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfFilePath: Int = getColumnIndexOrThrow(_stmt, "filePath")
        val _columnIndexOfFolderId: Int = getColumnIndexOrThrow(_stmt, "folderId")
        val _columnIndexOfDurationMillis: Int = getColumnIndexOrThrow(_stmt, "durationMillis")
        val _columnIndexOfSizeBytes: Int = getColumnIndexOrThrow(_stmt, "sizeBytes")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<NoteEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NoteEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpFilePath: String
          _tmpFilePath = _stmt.getText(_columnIndexOfFilePath)
          val _tmpFolderId: Long?
          if (_stmt.isNull(_columnIndexOfFolderId)) {
            _tmpFolderId = null
          } else {
            _tmpFolderId = _stmt.getLong(_columnIndexOfFolderId)
          }
          val _tmpDurationMillis: Long
          _tmpDurationMillis = _stmt.getLong(_columnIndexOfDurationMillis)
          val _tmpSizeBytes: Long
          _tmpSizeBytes = _stmt.getLong(_columnIndexOfSizeBytes)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item = NoteEntity(_tmpId,_tmpTitle,_tmpFilePath,_tmpFolderId,_tmpDurationMillis,_tmpSizeBytes,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeNotesInFolder(folderId: Long): Flow<List<NoteEntity>> {
    val _sql: String = "SELECT * FROM notes WHERE folderId = ? ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("notes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, folderId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfFilePath: Int = getColumnIndexOrThrow(_stmt, "filePath")
        val _columnIndexOfFolderId: Int = getColumnIndexOrThrow(_stmt, "folderId")
        val _columnIndexOfDurationMillis: Int = getColumnIndexOrThrow(_stmt, "durationMillis")
        val _columnIndexOfSizeBytes: Int = getColumnIndexOrThrow(_stmt, "sizeBytes")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<NoteEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NoteEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpFilePath: String
          _tmpFilePath = _stmt.getText(_columnIndexOfFilePath)
          val _tmpFolderId: Long?
          if (_stmt.isNull(_columnIndexOfFolderId)) {
            _tmpFolderId = null
          } else {
            _tmpFolderId = _stmt.getLong(_columnIndexOfFolderId)
          }
          val _tmpDurationMillis: Long
          _tmpDurationMillis = _stmt.getLong(_columnIndexOfDurationMillis)
          val _tmpSizeBytes: Long
          _tmpSizeBytes = _stmt.getLong(_columnIndexOfSizeBytes)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item = NoteEntity(_tmpId,_tmpTitle,_tmpFilePath,_tmpFolderId,_tmpDurationMillis,_tmpSizeBytes,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeNoteById(noteId: Long): Flow<NoteEntity?> {
    val _sql: String = "SELECT * FROM notes WHERE id = ?"
    return createFlow(__db, false, arrayOf("notes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, noteId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfFilePath: Int = getColumnIndexOrThrow(_stmt, "filePath")
        val _columnIndexOfFolderId: Int = getColumnIndexOrThrow(_stmt, "folderId")
        val _columnIndexOfDurationMillis: Int = getColumnIndexOrThrow(_stmt, "durationMillis")
        val _columnIndexOfSizeBytes: Int = getColumnIndexOrThrow(_stmt, "sizeBytes")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: NoteEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpFilePath: String
          _tmpFilePath = _stmt.getText(_columnIndexOfFilePath)
          val _tmpFolderId: Long?
          if (_stmt.isNull(_columnIndexOfFolderId)) {
            _tmpFolderId = null
          } else {
            _tmpFolderId = _stmt.getLong(_columnIndexOfFolderId)
          }
          val _tmpDurationMillis: Long
          _tmpDurationMillis = _stmt.getLong(_columnIndexOfDurationMillis)
          val _tmpSizeBytes: Long
          _tmpSizeBytes = _stmt.getLong(_columnIndexOfSizeBytes)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _result = NoteEntity(_tmpId,_tmpTitle,_tmpFilePath,_tmpFolderId,_tmpDurationMillis,_tmpSizeBytes,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getNoteById(noteId: Long): NoteEntity? {
    val _sql: String = "SELECT * FROM notes WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, noteId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfFilePath: Int = getColumnIndexOrThrow(_stmt, "filePath")
        val _columnIndexOfFolderId: Int = getColumnIndexOrThrow(_stmt, "folderId")
        val _columnIndexOfDurationMillis: Int = getColumnIndexOrThrow(_stmt, "durationMillis")
        val _columnIndexOfSizeBytes: Int = getColumnIndexOrThrow(_stmt, "sizeBytes")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: NoteEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpFilePath: String
          _tmpFilePath = _stmt.getText(_columnIndexOfFilePath)
          val _tmpFolderId: Long?
          if (_stmt.isNull(_columnIndexOfFolderId)) {
            _tmpFolderId = null
          } else {
            _tmpFolderId = _stmt.getLong(_columnIndexOfFolderId)
          }
          val _tmpDurationMillis: Long
          _tmpDurationMillis = _stmt.getLong(_columnIndexOfDurationMillis)
          val _tmpSizeBytes: Long
          _tmpSizeBytes = _stmt.getLong(_columnIndexOfSizeBytes)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _result = NoteEntity(_tmpId,_tmpTitle,_tmpFilePath,_tmpFolderId,_tmpDurationMillis,_tmpSizeBytes,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun moveToFolder(noteId: Long, folderId: Long?) {
    val _sql: String = "UPDATE notes SET folderId = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        if (folderId == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindLong(_argIndex, folderId)
        }
        _argIndex = 2
        _stmt.bindLong(_argIndex, noteId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun rename(noteId: Long, title: String) {
    val _sql: String = "UPDATE notes SET title = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, title)
        _argIndex = 2
        _stmt.bindLong(_argIndex, noteId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
