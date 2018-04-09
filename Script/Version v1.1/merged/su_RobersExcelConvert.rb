require 'rubyXL'
require 'inifile'
require_relative 'su_RobersExcelConvert/classes/materialHandler'
require_relative 'su_RobersExcelConvert/classes/entityHandler'
require_relative 'su_RobersExcelConvert/classes/rectangle'
require_relative 'su_RobersExcelConvert/classes/ConnectionLoader'

#require_relative 'su_RobersExcelConvert/main'

$path = File.dirname(__FILE__) + '/su_RobersExcelConvert'
$texturePath = $path + '/textures'
$EntityHandler = EntityHandler.new


# method: isLoadede
# parameters: -none-
# returns: -none-
#
# A simple test method in the Ruby-Console of SketchUp to test whether the Plugin is loaded
def isLoaded
  puts "Successfully loaded."
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
    #materials = MaterialHandler.new([Sketchup.active_model.materials[0]])
    materials = $MaterialIdentifier.identifyMaterialHandler(elements[count]["Materialgruppe"], elements[count]["Werkstoff"])
    puts "Construct a '#{elements[count]["Bezeichnung"]}:#{elements[count]["Bauteil"]}' called '#{name}' with [x,y,z]=#{bounds} #{anzahl} times, starting at y = #{lastY.mm}"
    createNRectangles(anzahl, Rectangle.new(height.mm, width.mm, depth.mm, Geom::Vector3d.new(0, lastY.mm, 0), materials, name))
    lastY = lastY + height + 50
  end

  #$EntityHandler.drawAll
end

def preloadMaterials
  files = Dir[File.expand_path("..", __dir__) + "/textures/**"]
  #puts File.basename files[0]

  files.each do |file|
    filename = File.basename(file).split(".")[0]
    # puts filename
    material = Sketchup.active_model.materials.add(filename)
    material.texture = file
    @materials[filename] = material
  end
end

def loadToolbar
  toolbar = UI::Toolbar.new("Robers Excel Convert")

  #readExcelCommand
  # readExcelCommand = UI::Command.new("Read Excel") {
  #   file = UI.openpanel("Choose *.xlsm-file", "D:/Dokumente/GitHub/Robers-GmbH---Excel-to-ScetchUp/Testdaten", "*.xlsm")
  #   if not file.nil? then
  #     UI.messagebox("Start reading the excel. This may take a few seconds.", type = MB_OK)
  #     readExcel file
  #     answer = UI.messagebox("Finished reading. Want to draw the entities now?", type = MB_YESNO)
  #     if answer == IDYES then
  #       $EntityHandler.drawAll
  #     end
  #   end
  # }
  # readExcelIcon = Sketchup.find_support_file("icon.png", "Plugins/su_RobersExcelConvert/Icons/")
  # readExcelCommand.large_icon = readExcelIcon
  # readExcelCommand.small_icon = readExcelIcon
  # readExcelCommand.tooltip = "Open a xlsm-file"
  # toolbar.add_item readExcelCommand
  #
  readExcelCommand = UI::Command.new("Read Connection.ini") {
    file = $path + "/classes/connection.ini"
    if not file.nil? then
      UI.messagebox("Start reading the excel. This may take a few seconds.", type = MB_OK)
      readConnectionFile file
    end


  }
  readExcelIcon = Sketchup.find_support_file("icon.png", "Plugins/su_RobersExcelConvert/Icons/")
  readExcelCommand.large_icon = readExcelIcon
  readExcelCommand.small_icon = readExcelIcon
  readExcelCommand.tooltip = "Open a xlsm-file"
  toolbar.add_item readExcelCommand

  UI.start_timer(0.1, false) {
    toolbar.restore
  }
end


def readConnectionFile(file)
  cl = ConnectionLoader.new(file)
  cl.readElements
end

# method: load
# parameter: -none-
# returns: -none-
#
# Loads all the essential parts of the program in order to use the plugin
def load
  SKETCHUP_CONSOLE.show

  preloadMaterials
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