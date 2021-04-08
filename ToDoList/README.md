## Name

ToDoList (Prosjektoppgave I)

## Description

This app was developed as part of our first project task in IKT205. It is developed using the programming language Kotlin as well as Firestore as its backend database. It contains two screens (activities) where the first one is an overview of all the categories and the second one is detailed overview of all tasks that are in a particular category.

## How It Works

1. When you open the app, it gets the categories from Firestore database and adds them to the scrollview (screen 1)
- By entering a category name and pressing the "ADD" button, the user adds a new category to the database and list of categories
Example og the add category function:
´´´
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
´´´
- By pressing the "bin" icon a user deletes the category from the database and category list
- By pressing on a category the user goes to screen 2 where a detailed view of the tasks in that category is displayed

2. On the second screen there is the list name and a progress bar at the top, followed by the tasks in that category, below. Besides each task there is checkbox which updates task´s "done" field in the database to "true" if it is checked and "false" if it is not checked.
- Upon entering the second screen all tasks and progress for the particular category is received and added to the task list
- By entering a task name and pressing the "ADD TASK" button, the user adds a new task to the database and list of tasks
- By pressing the "DELETE DONE" button the user deletes all tasks from the database and list of tasks, that are checked 

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
