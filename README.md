# KFYourself
a kotlin port of joshuadoes's sanitization based swear filter

## Usage

```kotlin
// Initialization
val swearFilter = KFYourself("badword1", "badword2", "swearword")

// Check for Bad Words
val foundWords = swearFilter.check("This is a test message with badword1 in it.")
println(foundWords)  // Output: ["badword1"]

// Adding Bad Words
swearFilter.add("newbadword")

// Removing Bad Words
swearFilter.delete("badword1")

// List Active Bad Words
val activeWords = swearFilter.words()
println(activeWords)  // Output: ["badword2", "swearword", "newbadword"]

// Examples

// Basic Usage
val filter = KFYourself("example", "test")
val message = "This is a test message."
val tripped = filter.check(message)
println(tripped) // Output: ["test"]

// Leet Speak Normalization
val leetFilter = KFYourself("leet")
val leetMessage = "l33t is fun!"
val leetTripped = leetFilter.check(leetMessage)
println(leetTripped) // Output: ["leet"]

// Adding and Removing Bad Words
val dynamicFilter = KFYourself("initial")
dynamicFilter.add("additional", "badword")
println(dynamicFilter.words()) // Output: ["initial", "additional", "badword"]

dynamicFilter.delete("initial")
println(dynamicFilter.words()) // Output: ["additional", "badword"]
```
