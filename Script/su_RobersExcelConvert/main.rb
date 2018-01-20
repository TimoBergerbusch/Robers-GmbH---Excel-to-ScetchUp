require_relative 'classes/materialHandler'
require_relative 'classes/faceHandler'
require_relative 'classes/rectangle'

$faceHandler = FaceHandler.new

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

  model.materials.each {|x|
    puts x
  }
  d = Rectangle.new(50.mm, 400.mm, 1000.mm, Geom::Vector3d.new(10.mm, 10.mm, 10.mm), MaterialHandler.new([model.materials[0], model.materials[1]]))
  faces = d.draw(model)
end

def testFaceHandler
  model = Sketchup.active_model

  rec = Rectangle.new(50.mm, 400.mm, 1000.mm, Geom::Vector3d.new(0, 0, 0), MaterialHandler.new([model.materials[0]]))
  $faceHandler.addRectangle(rec)
  rec = Rectangle.new(50.mm, 400.mm, 1000.mm, Geom::Vector3d.new(425.mm, 0, 0), MaterialHandler.new([model.materials[0], model.materials[1]]))
  $faceHandler.addRectangle(rec)
  rec = Rectangle.new(50.mm, 400.mm, 1000.mm, Geom::Vector3d.new(0, 150.mm, 0), MaterialHandler.new([model.materials[0], model.materials[1], model.materials[2]]))
  $faceHandler.addRectangle(rec)
  rec = Rectangle.new(50.mm, 400.mm, 1000.mm, Geom::Vector3d.new(425.mm, 150.mm, 0), MaterialHandler.new(model.materials))
  $faceHandler.addRectangle(rec)
end

def createNRectangles(numberOfRec, rectangle)
  width = rectangle.width
  offset = rectangle.offset
  counter = 1
  $faceHandler.addRectangle(rectangle)

  while counter < numberOfRec
    rec = rectangle.clone
    rec.offset = Geom::Vector3d.new(counter*(width + 10.mm), 0, 0) + offset
    $faceHandler.addRectangle(rec)
    counter+=1
  end
end

# method: loadMaterials
# parameter: -none-
# returns: -none-
#
# Loads all the custom materials used within the project
def loadMaterials
  material1 = Sketchup.active_model.materials.add("Test Texture 1")
  material1.texture = "C:/Users/Timo Bergerbusch/AppData/Roaming/SketchUp/SketchUp 2018/SketchUp/Plugins/classes/texture1.jpg"
  material2 = Sketchup.active_model.materials.add("Test Texture 2")
  material2.texture = "C:/Users/Timo Bergerbusch/AppData/Roaming/SketchUp/SketchUp 2018/SketchUp/Plugins/classes/texture2.jpg"
  material3 = Sketchup.active_model.materials.add("Test Texture 3")
  material3.texture = "C:/Users/Timo Bergerbusch/AppData/Roaming/SketchUp/SketchUp 2018/SketchUp/Plugins/classes/texture3.jpg"
  material4 = Sketchup.active_model.materials.add("Test Texture 4")
  material4.texture = "C:/Users/Timo Bergerbusch/AppData/Roaming/SketchUp/SketchUp 2018/SketchUp/Plugins/classes/texture4.jpg"
  material5 = Sketchup.active_model.materials.add("Test Texture 5")
  material5.texture = "C:/Users/Timo Bergerbusch/AppData/Roaming/SketchUp/SketchUp 2018/SketchUp/Plugins/classes/texture5.jpg"
  material6 = Sketchup.active_model.materials.add("Test Texture 6")
  material6.texture = "C:/Users/Timo Bergerbusch/AppData/Roaming/SketchUp/SketchUp 2018/SketchUp/Plugins/classes/texture6.jpg"
end

# method: load
# parameter: -none-
# returns: -none-
#
# Loads all the essential parts of the program in order to use the plugin
def load
  SKETCHUP_CONSOLE.show

  loadMaterials
  #testFaceHandler

  UI.menu("Plugins").add_item("Create Rectangle") {
    testRectangle
  }


  UI.menu("Plugins").add_item("Draw FaceHandler Faces") {
    if $faceHandler.is_a? nil::NilClass
      puts "ERROR: FaceHandler is not initialized!"
    else
      $faceHandler.drawAll()
    end
  }
  UI.menu("Plugins").add_item("Create Cloned Rectangles") {
    model = Sketchup.active_model

    rec = Rectangle.new(50.mm, 400.mm, 1000.mm, Geom::Vector3d.new(0, 0, 0), MaterialHandler.new([model.materials[0]]))
    createNRectangles(3, rec)
    $faceHandler.drawAll

    #$faceHandler.createAndAddRectangle(50.mm, 400.mm, 1000.mm, Geom::Vector3d.new(410.mm, 0, 0), [model.materials[1], model.materials[0]])
  }

  UI.menu("Plugins").add_item("Create new Rectangle") {
    prompts = ["height:", "width: ",  "depth:", "offset:","offset(y-axis):","offset(z-axis):"]
    defaults = [50, 400, 1000,0,0,0]
    input = UI.inputbox(prompts, defaults, "Please enter the properties","Create Rectangle")
    puts input
    $faceHandler.createAndAddRectangle(input[0].mm,input[1].mm,input[2].mm,Geom::Vector3d.new(input[3],input[4],input[5]),MaterialHandler.new([]))

    #rec = Rectangle.new(50.mm, 400.mm, 1000.mm, Geom::Vector3d.new(0, 0, 0), MaterialHandler.new([model.materials[0]]))

    $faceHandler.drawAll()
  }
end

# execute the load
load