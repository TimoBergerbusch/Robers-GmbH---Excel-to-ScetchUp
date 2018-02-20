require 'rubyXL'
require 'inifile'
require_relative 'su_RobersExcelConvert/classes/materialHandler'
require_relative 'su_RobersExcelConvert/classes/entityHandler'
require_relative 'su_RobersExcelConvert/classes/rectangle'
require_relative 'su_RobersExcelConvert/classes/excelReader'
require_relative 'su_RobersExcelConvert/classes/element_identifier'
require_relative 'su_RobersExcelConvert/classes/constants_loader'

#require_relative 'su_RobersExcelConvert/main'

$path = File.dirname(__FILE__) + '/su_RobersExcelConvert'
$texturePath = $path + '/textures'
$EntityHandler = EntityHandler.new
$er = ExcelReader.new
$ei = ElementIdentifier.new
$ConstantsLoader = ConstantsLoader.new


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
  $EntityHandler.addRectangle(rectangle)

  #puts "Height: #{offset.z}"

  while counter < numberOfRec
    rec = rectangle.clone
    rec.offset = Geom::Vector3d.new(counter * (width + 10.mm), 0, 0) + offset
    $EntityHandler.addRectangle(rec)
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

  #$EntityHandler.drawAll
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

def loadToolbar
  #UI.menu("Plugins").add_item("Test Toolbar") {
  toolbar = UI::Toolbar.new("Testtoolbar")

  #readExcelCommand
  readExcelCommand = UI::Command.new("Read Excel") {
    file = UI.openpanel("Choose *.xlsm-file", "D:/Dokumente/GitHub/Robers-GmbH---Excel-to-ScetchUp/Testdaten", "*.xlsm")
    if not file.nil? then
      UI.messagebox("Start reading the excel. This may take a few seconds.", type=MB_OK)
      readExcel file
      answer = UI.messagebox("Finished reading. Want to draw the entities now?",type=MB_YESNO)
      if answer == IDYES then
        $EntityHandler.drawAll
      end
    end
  }
  readExcelIcon = Sketchup.find_support_file("icon.png", "Plugins/su_RobersExcelConvert/Icons/")
  readExcelCommand.large_icon = readExcelIcon
  readExcelCommand.small_icon = readExcelIcon
  readExcelCommand.tooltip = "Open a xlsm-file"
  toolbar.add_item readExcelCommand

  #DrawAllCommand
  drawEntityHandlerCommand = UI::Command.new("Draw read Entities") {
    $EntityHandler.drawAll
  }
  drawEntityHandlerIcon = Sketchup.find_support_file("paintbrush.png", "Plugins/su_RobersExcelConvert/Icons/")
  drawEntityHandlerCommand.large_icon = drawEntityHandlerIcon
  drawEntityHandlerCommand.small_icon = drawEntityHandlerIcon
  drawEntityHandlerCommand.tooltip = "Draws Entities from prev. read Excel-file"
  toolbar.add_item drawEntityHandlerCommand

  #clearEntityHandler
  clearEntityHandlerCommand = UI::Command.new("Erase created Entities") {
    $EntityHandler.deleteAll
  }
  clearEntityHandlerIcon = Sketchup.find_support_file("garbage.png", "Plugins/su_RobersExcelConvert/Icons/")
  clearEntityHandlerCommand.large_icon = clearEntityHandlerIcon
  clearEntityHandlerCommand.small_icon = clearEntityHandlerIcon
  clearEntityHandlerCommand.tooltip = "Erases Entities created through the RobersExcelConvert-tool"
  toolbar.add_item clearEntityHandlerCommand

  UI.start_timer(0.1, false) {
    toolbar.restore
  }
end

def readExcel(excelPath)

  elements = $er.loadDocument(excelPath)

  entities = $ei.identifyElements(elements)

  drawElements(elements, entities)
end

# method: load
# parameter: -none-
# returns: -none-
#
# Loads all the essential parts of the program in order to use the plugin
def load
  SKETCHUP_CONSOLE.show

  loadMaterials
  loadToolbar

  UI.menu("Plugins").add_item("Create Rectangle") {
    testRectangle
  }

  UI.menu("Plugins").add_item("Create Cloned Rectangles") {
    model = Sketchup.active_model

    rec = Rectangle.new(50.mm, 400.mm, 1000.mm, Geom::Vector3d.new(0, 0, 0), MaterialHandler.new([model.materials[0]]))
    createNRectangles(3, rec)
    $EntityHandler.drawAll

    #$EntityHandler.createAndAddRectangle(50.mm, 400.mm, 1000.mm, Geom::Vector3d.new(410.mm, 0, 0), [model.materials[1], model.materials[0]])
  }

  UI.menu("Plugins").add_item("Read and Draw File") {
    #file = UI.openpanel("title", "D:/Dokumente/GitHub/Robers-GmbH---Excel-to-ScetchUp/Testdaten", "*.xlsm")
    #if not file.nil? then
    #  readExcel file
    #  $EntityHandler.drawAll
    #end

    $EntityHandler.deleteAll
  }


  #$ConstantsLoader.test

end

# execute the load
load