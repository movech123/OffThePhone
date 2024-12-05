import androidx.room.Database
import androidx.room.RoomDatabase
import com.cs407.offthephone.Task
import com.cs407.offthephone.TaskDao


@Database(
    entities = [Task::class],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract val dao: TaskDao

}