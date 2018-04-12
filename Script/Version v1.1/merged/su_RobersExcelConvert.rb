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

def loadToolbar
  toolbar = UI::Toolbar.new("Robers Excel Convert")

  readExcelCommand = UI::Command.new("Read Connection.ini") {
    file = $path + "/classes/connection.ini"
    if not file.nil? then
      # UI.messagebox("Start reading the excel. This may take a few seconds.", type = MB_OK)
      readConnectionFile file
    end


  }
  readExcelIcon = Sketchup.find_support_file("paintbrush.png", "Plugins/su_RobersExcelConvert/Icons/")
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
  # cl.readGeneral
  # cl.readElements
  #rectangles = cl.getRectangles
  rectangles = cl.readFile

  $EntityHandler.deleteAll
  rectangles.each_key {|key|
    $EntityHandler.addRectangle(rectangles[key])
  }
  #$EntityHandler.drawAll
end

# method: load
# parameter: -none-
# returns: -none-
#
# Loads all the essential parts of the program in order to use the plugin
def load
  SKETCHUP_CONSOLE.show

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
