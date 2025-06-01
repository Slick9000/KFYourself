# KFYourself
a kotlin port of joshuadoes' sanitization based swear filter

note: special character pattern filtering and spaced pattern filtering are enabled by default and have no option to be disabled.

## Usage

```kotlin
// Initialization
val swearFilter = KFYourself("shit")

// Check for Bad Words
val foundWords = swearFilter.check("This is a test message with shit in it.")
println(foundWords)  // Output: ["shit"]

// Adding Bad Words
swearFilter.add("shitter")

// Removing Bad Words
swearFilter.delete("shitter")

// List Active Bad Words
val activeWords = swearFilter.words()
println(activeWords)  // Output: ["shit", "shitter"]

// Leet Speak Normalization
val leetFilter = KFYourself("leet")
val leetMessage = "l33t is fun!"
val leetTripped = leetFilter.check(leetMessage)
println(leetTripped) // Output: ["leet"]
```
