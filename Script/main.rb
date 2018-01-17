require_relative 'classes/rectangle'

# method: isLoadede
# parameters: -none-
# returns: -none-
#
# A simple test method in the Ruby-Console of SketchUp to test whether the Plugin is loaded
def isLoaded
  puts "Successfully loaded."
end

# method: testRectangle
# parameters: -none-
# returns: -none-
#
# A test method that creates a rectangle with fixed values.
# This method can be refined to be a generic method to create rectangles from the excel-file
def testRectangle
  model = Sketchup.active_model
  #entities = model.active_entities

  model.materials.each{|x|
    puts x
  }
  d = Rectangle.new(50.mm , 400.mm, 1000.mm, Geom::Vector3d.new(10.mm,10.mm,10.mm), [model.materials[0],model.materials[1]])
  faces = d.draw(model)
end

# method: loadMaterials
# parameter: -none-
# returns: -none-
#
# Loads all the custom materials used within the project
def loadMaterials
  material1 = Sketchup.active_model.materials.add("Test Material Wood")
  material1.texture = "C:/Users/Timo Bergerbusch/AppData/Roaming/SketchUp/SketchUp 2018/SketchUp/Plugins/classes/texture.jpg"
  material2 = Sketchup.active_model.materials.add("Test Material Iron")
  material2.texture = "C:/Users/Timo Bergerbusch/AppData/Roaming/SketchUp/SketchUp 2018/SketchUp/Plugins/classes/texture2.jpg"
end

# method: load
# parameter: -none-
# returns: -none-
#
# Loads all the essential parts of the program in order to use the plugin
def load
  SKETCHUP_CONSOLE.show

  loadMaterials

  UI.menu("Plugins").add_item("Create Rectangle") {
    testRectangle
  }

  UI.menu("Plugins").add_item("Create and Move Rectangle") {
    #puts $face.material
    #rec = testRectangle
    #Sketchup.active_model.entities.transform_by_vectors([rec], [[0,0,10]])
    #Sketchup.active_model.entities.transform_entities(Geom::Transformation.new([10,0,0]), rec)
  }
end

# execute the load
load