package compose.project.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun AndroidViewTest() {

}

@Composable
fun CustomView() {
    var selectedItem by remember { mutableStateOf(0) }

    // Adds view to Compose
    AndroidView(
        modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
        factory = { context ->
            // Creates view
            EditText(context).apply {
                // Sets up listeners for View -> Compose communication
                setOnClickListener {
                    selectedItem = 1
                }
            }
        },
        update = { view ->
            // View's been inflated or state read in this block has been updated
            // Add logic here if necessary

            // As selectedItem is read here, AndroidView will recompose
            // whenever the state changes
            // Example of Compose -> View communication
            view.selectedItem = selectedItem
        }
    )
}

@Composable
fun ContentExample() {
    Column(Modifier.fillMaxSize()) {
        Text("Look at this CustomView!")
        CustomView()
    }
}