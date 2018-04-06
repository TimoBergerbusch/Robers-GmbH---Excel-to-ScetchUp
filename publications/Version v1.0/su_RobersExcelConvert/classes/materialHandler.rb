# The MaterialHandler is used to handle the different materials to the different faces.
# Based on the materials given within the constructor, the MaterialHandler assigns the faces the materials.
# It can work with 0,1,2,3 and 6 materials. Everything else is not defined and therefore results in an error
class MaterialHandler

  # getter and setter
  attr_accessor :materials

  # constructor
  # parameters:
  #   materials   SketchUp::Material[]    a Material-array
  #
  # Initializes a new MaterialHandler and based on the length of the array:
  #   Depending on the length the faces get the following index:
  #
  #   Array.length  front   back  left    right   top   bottom
  #   0             nil     nil   nil     nil     nil   nil
  #   1             0       0     0       0       0     0
  #   2             0       0     1       1       1     1
  #   3             0       0     2       2       1     1
  #   6             0       1     2       3       4     5
  def initialize(materials)
    @material = Hash.new
    if materials.length > 0
      @material["front"] = materials[0]
      @material["back"] = materials[0]
    end

    case materials.length
      when 0
        puts "WARNING: MaterialHandler (Code 0x02): No materials"
      when 1
        @material["left"] = materials[0]
        @material["right"] = materials[0]
        @material["top"] = materials[0]
        @material["bottom"] = materials[0]
      when 2
        @material["left"] = materials[1]
        @material["right"] = materials[1]
        @material["top"] = materials[1]
        @material["bottom"] = materials[1]
      when 3
        @material["top"] = materials[1]
        @material["bottom"] = materials[1]
        @material["left"] = materials[2]
        @material["right"] = materials[2]
      when 6
        @material["back"] = materials[1]
        @material["left"] = materials[2]
        @material["right"] = materials[3]
        @material["top"] = materials[4]
        @material["bottom"] = materials[5]
      else
        puts "ERROR: MaterialHandler(Code 0x01): The number of materials has to be 1,2,3 or 6"
    end
  end

  # method: get(name)
  # parameter: name   String    the name of the face
  # returns: the material assigned to that face
  #
  # It returns the material assigned to the face based on the name, which is on of:
  #   front, back, left, right, top, bottom
  def get(name)
    return @material[name]
  end

end
