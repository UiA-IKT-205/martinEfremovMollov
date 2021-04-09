## Name

ToDoList (Prosjektoppgave I)

## Description

This app was developed as part of our first project task in IKT205. It is developed using the programming language Kotlin and it uses Firestore as its backend database. It has two screens (activities) where the first one is an overview of all the categories and the second one is detailed overview of all tasks that are in a particular category.

## How It Works

1. When you open the app, it gets the categories from Firestore database and adds them to a list called "Cat", which is short for category (screen 1)
- By entering a category name and pressing the "ADD" button, the user adds a new category to the database and list of categories.
Example of the add category function:

```kotlin
private fun addCat(category: String) {
        val cat = Cat(category)
        val db = Firebase.firestore
        val ex = hashMapOf(
            "exists" to 1
        )

        db.collection("Categories").document(category)
            .set(ex)
            .addOnSuccessListener {
                Log.d(TAG, "Exists ref added with ID: $category")
                db.collection("Categories").document(category)
                    .set(cat)
                    .addOnSuccessListener {
                        Log.d(TAG, "Cat added with ID: $cat")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding Cat", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding exists ref", e)
            }

        CatDepositoryManager.instance.addCat(cat)
    }
```
UI for adding a category:

<img src="https://github.com/UiA-IKT-205/martinEfremovMollov/blob/main/ToDoList/examples/Screenshot%202021-04-09%20at%2012.10.41.png" alt="drawing" width="250"/>

- By pressing the "bin" icon a user deletes the category from the database and category list.
Example of the delete category function:
```kotlin
deleteBt.setOnClickListener {
                val TAG = "ToDoListTasks"

                val db = Firebase.firestore

                // Deletes category from Firestore //
                // --------------------------------------------------------------------------------------- //
                db.collection("Categories")
                    .document(title.text as String)
                    .delete()
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                // --------------------------------------------------------------------------------------- //
                val remove = Cat(title.text as String)
                CatDepositoryManager.instance.removeCat(remove)
            }
```
- By pressing on a category the user goes to screen 2 where a detailed view of the tasks in that category is displayed
Example of the open category details function. The intent is used to start the second activity:
```kotlin
private fun onCatClicked(cat: Cat): Unit {
        CatHolder.PickedCat = cat

        val intent =Intent(this, CatDetailsActivity::class.java)

        startActivity(intent)
    }
```

2. On the second screen there is the list name and a progress bar at the top, followed by the tasks in that category, below. Besides each task there is checkbox which updates task´s "done" field in the database to "true" if it is checked and "false" if it is not checked.
- Upon entering the second screen all tasks and progress for the particular category is received and added to the task list.
```kotlin
            // Imports tasks from Firestore
            // ------------------------------------------------------------------------ //
            db.collection("Categories")
                .document(receivedCatFormatted.replace(")", ""))
                .collection(receivedCatFormatted.replace(")", ""))
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        val dataForm = document.data.toString().replace("{done=", "")
                        val formattedDone = dataForm.replace("}", "")
                        println(formattedDone)
                        var taskFirebase: Tasks
                        if (formattedDone == "true") {
                            taskFirebase = Tasks(document.id, isChecked = true)
                        } else {
                            taskFirebase = Tasks(document.id, isChecked = false)
                        }

                        TaskDepositoryManager.instance.addTask(taskFirebase)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
```
In addition it also actively listens for changes in the progrss bar value and updates it:
```kotlin
        db.collection("Progress")
            .document(receivedCatFormatted.replace(")", ""))
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: ${snapshot.data}")
                    val progress = snapshot.data.toString().replace("{progress=", "")
                    val formattedProgress = progress.replace("}", "")
                    binding.progressBarSecond.progress = formattedProgress.toInt()
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
```
- By entering a task name and pressing the "ADD TASK" button, the user adds a new task to the database and list of tasks
```kotlin
        btnAddTodo.setOnClickListener {
            val todoTitle = etTodoTitle.text.toString()

            if(todoTitle.isNotEmpty()) {
                var todo = Tasks(todoTitle, false)
                val receivedCatFormatted = cat.toString().replace("Cat(category=", "")
                val todoy = hashMapOf(

                    "done" to false
                )

                db.collection("Categories")
                    .document(receivedCatFormatted.replace(")", ""))
                    .collection(receivedCatFormatted.replace(")", ""))
                    .document(todoTitle)
                    .set(todoy)
                    .addOnSuccessListener {
                        Log.d(TAG, "To-Do task added with ID: $todoTitle")
                        todo = Tasks(todoTitle, false)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding task", e)
                    }


                TaskDepositoryManager.instance.addTask(todo)
                etTodoTitle.text.clear()
            }
        }
```
UI for adding a task to category:

<img src="https://github.com/UiA-IKT-205/martinEfremovMollov/blob/main/ToDoList/examples/Screenshot%202021-04-09%20at%2012.11.08.png" alt="drawing" width="250"/>

- By pressing the "DELETE DONE" button the user deletes all tasks from the database and list of tasks, that are checked
```kotlin
        btnDeleteDoneTodos.setOnClickListener {
            val receivedBookFormatted = cat.toString().replace("Cat(category=", "")

            TaskDepositoryManager.instance.deleteDoneTasks()

            db.collection("Categories")
                .document(receivedBookFormatted.replace(")", ""))
                .collection(receivedBookFormatted.replace(")", ""))
                .whereEqualTo("done", true)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        db.collection("Categories")
                            .document(receivedBookFormatted.replace(")", ""))
                            .collection(receivedBookFormatted.replace(")", ""))
                            .document(document.id)
                            .delete()
                            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }
```

## Visuals

Screen 1:
- Overview of available lists (categories)
- User can delete lists (categories)
- User can create a new list (category)
- Once the user clicks a list a detailed view is presented

<img src="https://github.com/UiA-IKT-205/martinEfremovMollov/blob/main/ToDoList/examples/Screenshot_1617882085.png" alt="drawing" width="250"/>

Screen 2:
- Displays list name and progress
- Checkbox for each list item/task (indicates status of that item)
- Button for deleting done (checked) item/s
- Button for adding item/task

<img src="https://github.com/UiA-IKT-205/martinEfremovMollov/blob/main/ToDoList/examples/Screenshot_1617882097.png" alt="drawing" width="250"/>

## Installation

To install the app via Firebase App distribution click [HERE]()

## Author

Martin Efremov Mollov

## License

[MIT](https://choosealicense.com/licenses/mit/)

## Project status

Currently the app works as intended. The only known bug, as of now, is when a user deletes all done tasks the progress bar goes to 100, however when you check and uncheck an item it goes back to normal. This is being fixed in the next commit.
