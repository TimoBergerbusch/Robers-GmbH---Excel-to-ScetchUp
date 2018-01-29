require 'rubyXL'
require 'inifile'
#require 'tk'
require_relative 'su_RobersExcelConvert/classes/materialHandler'
require_relative 'su_RobersExcelConvert/classes/faceHandler'
require_relative 'su_RobersExcelConvert/classes/rectangle'
require_relative 'su_RobersExcelConvert/classes/excelReader'
require_relative 'su_RobersExcelConvert/classes/element_identifier'

#require_relative 'su_RobersExcelConvert/main'

$texturePath = File.dirname(__FILE__) + '/su_RobersExcelConvert/textures'
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

  #model.materials.each {|x|
  #  puts x
  #}

  d = Rectangle.new(50.mm, 400.mm, 1000.mm, Geom::Vector3d.new(10.mm, 10.mm, 10.mm), MaterialHandler.new([model.materials[0], model.materials[1]]))
  faces = d.draw(model)
end

def createNRectangles(numberOfRec, rectangle)
  width = rectangle.width
  offset = rectangle.offset
  counter = 1
  $faceHandler.addRectangle(rectangle)

  #puts "Height: #{offset.z}"

  while counter < numberOfRec
    rec = rectangle.clone
    rec.offset = Geom::Vector3d.new(counter * (width + 10.mm), 0, 0) + offset
    $faceHandler.addRectangle(rec)
    counter += 1
  end
end

def drawElements(elements, entities)
  lastY = 0.mm

  for count in 0..elements.length - 1
    anzahl = elements[count]["Anz."].to_i
    bounds = entities[count]
    width = bounds[0]
    height = bounds[2]
    depth = bounds[1]
    name = "CompType#{count}"
    materials = MaterialHandler.new([Sketchup.active_model.materials[0]])
    puts "Construct a '#{elements[count]["Bezeichnung"]}:#{elements[count]["Bauteil"]}' called '#{name}' with [x,y,z]=#{bounds} #{anzahl} times, starting at y = #{lastY.mm}"
    createNRectangles(anzahl, Rectangle.new(height.mm, width.mm, depth.mm, Geom::Vector3d.new(0, lastY.mm, 0), materials, name))
    lastY = lastY + height + 50
  end

  $faceHandler.drawAll
end

# method: loadMaterials
# parameter: -none-
# returns: -none-
#
# Loads all the custom materials used within the project
def loadMaterials
  #TODO: load all possible materials
  material1 = Sketchup.active_model.materials.add("Test Texture 1")
  material1.texture = "#{$texturePath}/texture1.jpg"
  material2 = Sketchup.active_model.materials.add("Test Texture 2")
  material2.texture = "#{$texturePath}/texture2.jpg"
  material3 = Sketchup.active_model.materials.add("Test Texture 3")
  material3.texture = "#{$texturePath}/texture3.jpg"
  material4 = Sketchup.active_model.materials.add("Test Texture 4")
  material4.texture = "#{$texturePath}/texture4.jpg"
  material5 = Sketchup.active_model.materials.add("Test Texture 5")
  material5.texture = "#{$texturePath}/texture5.jpg"
  material6 = Sketchup.active_model.materials.add("Test Texture 6")
  material6.texture = "#{$texturePath}/texture6.jpg"
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

  UI.menu("Plugins").add_item("Create Cloned Rectangles") {
    model = Sketchup.active_model

    rec = Rectangle.new(50.mm, 400.mm, 1000.mm, Geom::Vector3d.new(0, 0, 0), MaterialHandler.new([model.materials[0]]))
    createNRectangles(3, rec)
    $faceHandler.drawAll

    #$faceHandler.createAndAddRectangle(50.mm, 400.mm, 1000.mm, Geom::Vector3d.new(410.mm, 0, 0), [model.materials[1], model.materials[0]])
  }

  UI.menu("Plugins").add_item("Read Excel") {
    #path = File.expand_path (File.dirname(__FILE__)+'/su_RobersExcelConvert')
    er = ExcelReader.new
    elements = er.loadDocument(File.dirname(__FILE__) + '/su_RobersExcelConvert' + '/real_test1.xlsm')

    ei = ElementIdentifier.new
    entities = ei.identifyElements(elements)

    drawElements(elements, entities)
  }

  #UI.menu("Plugins").add_item("Create new Rectangle") {
  #  prompts = ["height:", "width: ",  "depth:", "offset:","offset(y-axis):","offset(z-axis):"]
  #  defaults = [50, 400, 1000,0,0,0]
  #  input = UI.inputbox(prompts, defaults, "Please enter the properties","Create Rectangle")
  #  puts input
  #  $faceHandler.createAndAddRectangle(input[0].mm,input[1].mm,input[2].mm,Geom::Vector3d.new(input[3].mm,input[4].mm,input[5].mm),MaterialHandler.new([]))
  #
  #  #rec = Rectangle.new(50.mm, 400.mm, 1000.mm, Geom::Vector3d.new(0, 0, 0), MaterialHandler.new([model.materials[0]]))
  #
  #  $faceHandler.drawAll()
  #}
end

# execute the load
load