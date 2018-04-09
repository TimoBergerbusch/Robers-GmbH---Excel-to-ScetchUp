require_relative 'rectangle'

class ConnectionLoader


  def initialize(file)
    @defaultGeneralElements = ["numberOfElements", "usedMaterials"]
    @defaultElementElements = ["name", "vorne", "hinten", "links", "rechts", "oben", "unten", "x-achse", "y-achse", "z-achse", "offX", "offY", "offZ"]
    @file = file
    @ini = nil
    @materials = Hash.new
    @data = Hash.new
    reloadFile
  end

  # TODO: ADD THESE TO SketchUp (UNCOMMENT)


  define_method(:reloadFile) do
    filePath = File.dirname(@file)
    if not File.exist? filePath then
      puts "WARNING(0x01): ConnectionLoader: No connection.ini found"
    else
      @ini = IniFile.load(@file)
    end
  end

  define_method(:isLoaded?) do
    return @ini != nil
  end

  define_method (:readElements) do
    if isLoaded? then
      generals = readGeneral
      @data["generals"] = generals
      for i in 1..generals["numberOfElements"]
        puts "Element number #{i}"
        element = readElement(i)
        # rectangle = createRectangleFromElement(element)
        @data["Element#{i}"] = element
        # @data["Rectangle#{i}"] = rectangle
      end
    end
  end

  def createRectangleFromElement(element)
    width = element["x-achse"]
    height = element["y-achse"]
    depth = element["z-achse"]
    offset = Geom::Vector3d.new(element["offX"], element["offY"], element["offZ"])
    name = element["name"]
    return Rectangle.new(width, height, depth, offset, name)
  end

  def readElement(number)
    element = Hash.new
    section = "Element#{number}"
    @defaultElementElements.each do |key|
      element[key] = @ini[section][key]
    end

    return element
  end

  define_method(:readGeneral) do
    generalMap = Hash.new

    @defaultGeneralElements.each do |key|
      if key == "usedMaterials" then
        generalMap[key] = getMaterialsAsArray(@ini["General"][key])
      else
        generalMap[key] = @ini["General"][key]
      end
    end

    return generalMap
  end

  define_method (:getMaterialsAsArray) do |string|
    return string.split("|")
  end

end