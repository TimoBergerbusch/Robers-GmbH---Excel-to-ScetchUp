class Rectangle

  attr_accessor :height, :width, :depth, :offset, :material, :faces

  # width => x-axis
  # height => z-axis
  # depth => y-axis

  def initialize(p_height, p_width, p_depth, p_offset, p_material)
    @height = p_height
    @width = p_width
    @depth = p_depth
    @offset = p_offset
    @material = p_material

    @faces = Hash.new
  end

  def toString
    return "Rectangle : width: #{@width}, height: #{@height}, depth: #{@depth}"
  end

  def createFrontFacePoints()
    points = []
    points << [@offset.x, @offset.z, @offset.y]
    points << [@offset.x + @width, @offset.z, @offset.y]
    points << [@offset.x + @width, @offset.z, @offset.y + @height]
    points << [@offset.x, @offset.z, @offset.y + @height]

    return points.reverse
  end

  def createAllPoints
    points = []
    points << [@offset.x, @offset.z, @offset.y]
    points << [@offset.x + @width, @offset.z, @offset.y]
    points << [@offset.x + @width, @offset.z, @offset.y - @height]
    points << [@offset.x, @offset.z, @offset.y - @height]
    points << [@offset.x, @offset.z + @depth, @offset.y]
    points << [@offset.x + @width, @offset.z + @depth, @offset.y]
    points << [@offset.x + @width, @offset.z + @depth, @offset.y - @height]
    points << [@offset.x, @offset.z + @depth, @offset.y - @height]

    return points
  end

  # Method: draw
  #
  def draw(model)
    # compute the front face
    points = createFrontFacePoints()

    # add the front face
    face = model.active_entities.add_face(points)


    # pushpull the depth of the rectangle
    face.pushpull(@depth)

    # compute all faces of the new rectangle
    faces = computeFaces(face)

    # label the faces
    identifyFaces(faces)

    # set textures
    @faces["vorne"].material = material[0]
    @faces["hinten"].material = material[0]
    @faces["links"].material = material[1]
    @faces["rechts"].material = material[1]
    @faces["oben"].material = material[1]
    @faces["unten"].material = material[1]

    # return
    return faces
  end

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

  def identifyFaces(faces)
    faces.each_with_index {|x, i|
      if x.is_a? Sketchup::Face
        if x.normal == Geom::Vector3d.new(0, 1, 0)
          @faces["hinten"] = x
        elsif x.normal == Geom::Vector3d.new(0, -1, 0)
          @faces["vorne"] = x
        elsif x.normal == Geom::Vector3d.new(1, 0, 0)
          @faces["rechts"] = x
        elsif x.normal == Geom::Vector3d.new(-1, 0, 0)
          @faces["links"] = x
        elsif x.normal == Geom::Vector3d.new(0, 0, 1)
          @faces["oben"] = x
        elsif x.normal == Geom::Vector3d.new(0, 0, -1)
          @faces["unten"] = x
        end
        #x.material = color[i]
      end
    }
  end

  def drawMultiFace(entities)
    points = createAllPoints
    faces = []
    faces << entities.add_face(points[0..3])
    faces << entities.add_face(points[4..7])
    faces << entities.add_face(points[1], points[0], points[4], points[5])
    faces << entities.add_face(points[0], points[4], points[7], points[3])
    faces << entities.add_face(points[1], points[5], points[6], points[2])
    faces << entities.add_face(points[2], points[3], points[7], points[6])
    return faces
  end
end