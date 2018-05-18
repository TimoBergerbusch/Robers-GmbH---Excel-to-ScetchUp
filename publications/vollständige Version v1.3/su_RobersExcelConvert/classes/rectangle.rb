# The Rectangle class is used in order to handle a rectangle shape within SketchUp.
# It stores all necessary information like width, height, depth, an offset vector,
# the material using the MaterialHandler and the different faces.
# The faces can be accessed individually by computing the corresponding faces to the original pushpulled one.
class Rectangle

  # Getter and Setter of the attributes
  attr_accessor :height, :width, :depth, :offset, :material, :faces

  # width => x-axis
  # height => z-axis
  # depth => y-axis

  # constructor
  # parameter:
  #   p_height  integer(mm)       the height (z-axis)
  #   p_width   integer(mm)       the width (x-axis)
  #   p_depth   integer(mm)       the depth (y-axis)
  #   offset    Geom::Vector3d    the offset of the rectangle(mm)
  #   material  MaterialHandler   a MaterialHandler-instance
  #   name      String            the name the group should have within SketchUp
  #
  # Initializes a new rectangle.
  #
  # Note: all the parameters are given in mm!
  def initialize(p_height, p_width, p_depth, p_offset, p_material, p_name = "Group")
    @height = p_height
    @width = p_width
    @depth = p_depth
    @offset = p_offset
    @material = p_material
    @name = p_name

    @gruppe = Sketchup.active_model.entities.add_group
    @gruppe.name = @name
    @faces = Hash.new
  end

  # method: toString
  # parameter: -none-
  # returns: String
  #
  # Creates a String with all the important information about the rectangle
  def toString
    return "Rectangle : width: #{@width}, height: #{@height}, depth: #{@depth}, offset: #{@offset}"
  end

  # method: createFrontFacePoints
  # parameter: -none-
  # returns: Geom::Point3d[] points
  #
  # Computes the points of the front face with respect to the given properties.
  #
  # Note: Since the points are listed counter clockwise the array gets reversed
  # before returned so that further the normal will point towards the front
  def createFrontFacePoints()
    points = []
    # points << [@offset.x, @offset.z, @offset.y]
    # points << [@offset.x + @width, @offset.z, @offset.y]
    # points << [@offset.x + @width, @offset.z, @offset.y + @height]
    # points << [@offset.x, @offset.z, @offset.y + @height]
    points << [@offset.x, @offset.y, @offset.z]
    points << [@offset.x + @width, @offset.y, @offset.z]
    points << [@offset.x + @width, @offset.y, @offset.z + @height]
    points << [@offset.x, @offset.y, @offset.z + @height]
    # points.each_with_index {|item, index|
    #   puts "#{index}:(#{item[0]},#{item[1]},#{item[2]})"
    # }

    return points.reverse
  end

  # method: draw
  # parameter: -none-
  # returns: the faces
  #
  # This method draws the rectangle into the model at a given offset.
  # It further identifies the different faces and sets the textures accordingly
  def draw(model)
    # compute the front face
    points = createFrontFacePoints()

    # add the front face
    if @gruppe.deleted?
      @gruppe = Sketchup.active_model.entities.add_group
      @gruppe.name = @name
    end
    face = @gruppe.entities.add_face(points)

    # pushpull the depth of the rectangle
    face.pushpull(@depth)

    # compute all faces of the new rectangle
    faces = computeFaces(face)

    # label the faces
    identifyFaces(faces)

    # set textures
    @faces["front"].material = @material.get("front")
    @faces["back"].material = @material.get("back")
    @faces["left"].material = @material.get("left")
    @faces["right"].material = @material.get("right")
    @faces["top"].material = @material.get("top")
    @faces["bottom"].material = @material.get("bottom")

    # return
    return faces
  end

  # method: computeFaces
  # parameter: face   Sketchup::Face    a face
  # returns: an array of faces
  #
  # Computes all connected faces of the original face
  def computeFaces(face)
    faces = []
    face.all_connected.each {|x|
      if x.is_a? Sketchup::Face
        #x.material = "red"
        faces << x
      end
    }
    #faces = faces.to_s
    return faces
  end

  # method: identifyFaces
  # parameter: faces    Sketchup::Face[]  the faces forming a rectangle
  # returns: -none-
  #
  # Sets the entries in the @faces-hash accordingly to the normal vectors of the face.
  def identifyFaces(faces)
    faces.each_with_index {|x, i|
      if x.is_a? Sketchup::Face
        if x.normal == Geom::Vector3d.new(0, 1, 0)
          @faces["back"] = x
        elsif x.normal == Geom::Vector3d.new(0, -1, 0)
          @faces["front"] = x
        elsif x.normal == Geom::Vector3d.new(1, 0, 0)
          @faces["right"] = x
        elsif x.normal == Geom::Vector3d.new(-1, 0, 0)
          @faces["left"] = x
        elsif x.normal == Geom::Vector3d.new(0, 0, 1)
          @faces["top"] = x
        elsif x.normal == Geom::Vector3d.new(0, 0, -1)
          @faces["bottom"] = x
        end
        #x.material = color[i]
      end
    }
  end

  # method: clone
  # parameter: -none-
  # returns: a rectangle
  #
  # Creates an exact copy of the instance called from including the name
  def clone
    return Rectangle.new(@height, @width, @depth, @offset, @material, @name)
  end

  def delete
    begin
      @gruppe.erase!
    rescue TypeError => error
      #don't do anything. It's ok
    end
    @faces = Hash.new
  end
end