#!/bin/sh
# Seeds the database with 10 contacts and their descriptions
# Assumes psql is available and environment variables POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_DB are set

set -e

echo "Seeding database with 10 contacts..."

# Function to get array element by index
get_name() { echo "Emma Silva|Sofia Costa|Ana Ferreira|Maria Santos|Beatriz Oliveira|Carolina Pereira|João Rodrigues|Miguel Almeida|Pedro Carvalho|Bruno Martins" | cut -d'|' -f$1; }
get_email() { echo "emma.silva@gmail.com|sofia.costa@outlook.com|ana.ferreira@hotmail.com|maria.santos@yahoo.com|beatriz.oliveira@gmail.com|carolina.pereira@outlook.com|joao.rodrigues@gmail.com|miguel.almeida@hotmail.com|pedro.carvalho@outlook.com|bruno.martins@gmail.com" | cut -d'|' -f$1; }
get_phone() { echo "+351912345671|+351923456782|+351934567893|+351945678904|+351956789015|+351967890126|+351978901237|+351989012348|+351990123459|+351901234560" | cut -d'|' -f$1; }
get_nickname() { echo "Em||Aninha||Bia|Carol|João||Pedrinho|Bruno" | cut -d'|' -f$1; }
get_birthday() { echo "1995-03-15|1992-07-22|1988-11-08|1990-05-14|1993-09-03|1991-12-18|1987-04-25|1994-08-12|1989-06-30|1996-10-07" | cut -d'|' -f$1; }
get_notes() { echo "Met at university, studying psychology|Colleague from work, great at marketing|Childhood friend from the neighborhood|Family friend, works as a nurse|Met at the gym, fitness enthusiast|Cousin's friend, loves traveling|Best friend since high school|Neighbor, works in IT|College roommate, now an engineer|Met at a concert, music lover" | cut -d'|' -f$1; }
get_address() { echo "Rua das Flores 15, Porto|Avenida da Liberdade 45, Lisboa|Rua do Sol 8, Braga|Praça da República 22, Coimbra|Rua Nova 33, Aveiro|Largo do Mercado 7, Faro|Rua da Paz 19, Viseu|Avenida Central 28, Leiria|Rua dos Pinheiros 12, Setúbal|Praça do Comércio 5, Évora" | cut -d'|' -f$1; }

echo "Seeding database with 10 contacts..."

for i in 1 2 3 4 5 6 7 8 9 10
do
  NAME=$(get_name $i)
  EMAIL=$(get_email $i)
  PHONE=$(get_phone $i)
  NICKNAME=$(get_nickname $i)
  BIRTHDAY=$(get_birthday $i)
  NOTES=$(get_notes $i)
  ADDRESS=$(get_address $i)

  # Escape single quotes in text fields to prevent SQL injection
  NAME=$(echo "$NAME" | sed "s/'/''/g")
  EMAIL=$(echo "$EMAIL" | sed "s/'/''/g")
  NICKNAME=$(echo "$NICKNAME" | sed "s/'/''/g")
  NOTES=$(echo "$NOTES" | sed "s/'/''/g")
  ADDRESS=$(echo "$ADDRESS" | sed "s/'/''/g")  # Set sex based on contact number (1-6 are girls, 7-10 are boys)
  if [ $i -le 6 ]; then
    SEX="FEMALE"
  else
    SEX="MALE"
  fi

  # Randomly select enum values for description
  AGES="CHILD ADOLESCENT YOUNG_ADULT ADULT ELDER"
  SKINTONES="DARK MEDIUM LIGHT"
  EYECOLORS="BROWN GREEN BLUE"
  HAIRTYPES="LONG MEDIUM SHORT NONE"
  HAIRCOLORS="BLACK BROWN GRAY WHITE BLONDE GINGER PAINTED NONE"

  # Get random selections using sh-compatible method
  AGE=$(echo $AGES | tr ' ' '\n' | shuf -n 1)
  SKINTONE=$(echo $SKINTONES | tr ' ' '\n' | shuf -n 1)
  EYECOLOR=$(echo $EYECOLORS | tr ' ' '\n' | shuf -n 1)
  HAIRTYPE=$(echo $HAIRTYPES | tr ' ' '\n' | shuf -n 1)
  HAIRCOLOR=$(echo $HAIRCOLORS | tr ' ' '\n' | shuf -n 1)
  FACIALHAIR=$(($(dd if=/dev/urandom bs=1 count=1 2>/dev/null | od -An -tu1) % 2))

  # Convert enum values to ordinals (0-based indexing)
  case $AGE in
    "CHILD") AGE_NUM=0 ;;
    "ADOLESCENT") AGE_NUM=1 ;;
    "YOUNG_ADULT") AGE_NUM=2 ;;
    "ADULT") AGE_NUM=3 ;;
    "ELDER") AGE_NUM=4 ;;
  esac

  case $SEX in
    "MALE") SEX_NUM=0 ;;
    "FEMALE") SEX_NUM=1 ;;
  esac

  case $SKINTONE in
    "DARK") SKINTONE_NUM=0 ;;
    "MEDIUM") SKINTONE_NUM=1 ;;
    "LIGHT") SKINTONE_NUM=2 ;;
  esac

  case $EYECOLOR in
    "BROWN") EYECOLOR_NUM=0 ;;
    "GREEN") EYECOLOR_NUM=1 ;;
    "BLUE") EYECOLOR_NUM=2 ;;
  esac

  case $HAIRTYPE in
    "LONG") HAIRTYPE_NUM=0 ;;
    "MEDIUM") HAIRTYPE_NUM=1 ;;
    "SHORT") HAIRTYPE_NUM=2 ;;
    "NONE") HAIRTYPE_NUM=3 ;;
  esac

  case $HAIRCOLOR in
    "BLACK") HAIRCOLOR_NUM=0 ;;
    "BROWN") HAIRCOLOR_NUM=1 ;;
    "GRAY") HAIRCOLOR_NUM=2 ;;
    "WHITE") HAIRCOLOR_NUM=3 ;;
    "BLONDE") HAIRCOLOR_NUM=4 ;;
    "GINGER") HAIRCOLOR_NUM=5 ;;
    "PAINTED") HAIRCOLOR_NUM=6 ;;
    "NONE") HAIRCOLOR_NUM=7 ;;
  esac

  # Convert facial hair to boolean
  if [ $FACIALHAIR -eq 1 ]; then
    FACIALHAIR_BOOL="true"
  else
    FACIALHAIR_BOOL="false"
  fi

  # Insert description and get its id (explicitly use nextval for the sequence)
  DESC_ID=$(PGPASSWORD="$POSTGRES_PASSWORD" psql -h db -U "$POSTGRES_USER" -d "$POSTGRES_DB" -t -A -c "INSERT INTO descriptions (id, age, eye_color, facial_hair, hair_color, hair_type, sex, skin_tone) VALUES (nextval('descriptions_seq'), $AGE_NUM, $EYECOLOR_NUM, $FACIALHAIR_BOOL, $HAIRCOLOR_NUM, $HAIRTYPE_NUM, $SEX_NUM, $SKINTONE_NUM) RETURNING id;" | head -1 | xargs)

  # Debug: Check if DESC_ID was retrieved successfully
  if [ -z "$DESC_ID" ]; then
    echo "Error: Failed to get description ID for contact $i"
    continue
  fi
  echo "Contact $i: DESC_ID=$DESC_ID"

  # Read image file and handle binary data
  IMAGE_PATH="images/${i}.jpg"
  if [ -f "$IMAGE_PATH" ]; then
    echo "Processing image for contact $i..."

    # Create a temporary SQL file for the image update
    TEMP_SQL="/tmp/update_contact_${i}.sql"

    if [ -z "$NICKNAME" ]; then
      # Insert without nickname if it's empty
      CONTACT_ID=$(PGPASSWORD="$POSTGRES_PASSWORD" psql -h db -U "$POSTGRES_USER" -d "$POSTGRES_DB" -t -A -c "INSERT INTO contacts (id, name, phone, birthday, notes, email, address, description_id) VALUES (nextval('contacts_seq'), '$NAME', '$PHONE', '$BIRTHDAY', '$NOTES', '$EMAIL', '$ADDRESS', $DESC_ID) RETURNING id;" | head -1 | xargs)
    else
      # Insert with nickname
      CONTACT_ID=$(PGPASSWORD="$POSTGRES_PASSWORD" psql -h db -U "$POSTGRES_USER" -d "$POSTGRES_DB" -t -A -c "INSERT INTO contacts (id, name, phone, nickname, birthday, notes, email, address, description_id) VALUES (nextval('contacts_seq'), '$NAME', '$PHONE', '$NICKNAME', '$BIRTHDAY', '$NOTES', '$EMAIL', '$ADDRESS', $DESC_ID) RETURNING id;" | head -1 | xargs)
    fi

    # Update with image data and thumbnail using a separate command
    echo "UPDATE contacts SET image_data = decode('" > "$TEMP_SQL"
    base64 -w 0 "$IMAGE_PATH" >> "$TEMP_SQL"
    echo "', 'base64'), thumbnail_data = decode('" >> "$TEMP_SQL"

    # Generate a smaller thumbnail (128x128) using available tools
    THUMB_PATH="/tmp/thumb_${i}.jpg"
    THUMBNAIL_CREATED=false

    # Try ImageMagick first
    if command -v convert >/dev/null 2>&1 && [ -f "$IMAGE_PATH" ]; then
      echo "Generating thumbnail for contact $i with ImageMagick..."
      if convert "$IMAGE_PATH" -resize 128x128\> -quality 80 "$THUMB_PATH" 2>/dev/null; then
        echo "ImageMagick thumbnail generated successfully"
        THUMBNAIL_CREATED=true
      else
        echo "ImageMagick failed, trying ffmpeg..."
      fi
    fi

    # Try ffmpeg as fallback
    if [ "$THUMBNAIL_CREATED" = false ] && command -v ffmpeg >/dev/null 2>&1 && [ -f "$IMAGE_PATH" ]; then
      echo "Generating thumbnail for contact $i with ffmpeg..."
      if ffmpeg -i "$IMAGE_PATH" -vf "scale=128:128:force_original_aspect_ratio=decrease" -y "$THUMB_PATH" 2>/dev/null; then
        echo "ffmpeg thumbnail generated successfully"
        THUMBNAIL_CREATED=true
      else
        echo "ffmpeg failed"
      fi
    fi

    # Use the thumbnail if created, otherwise use original
    if [ "$THUMBNAIL_CREATED" = true ] && [ -f "$THUMB_PATH" ]; then
      echo "Using generated thumbnail"
      ls -la "$THUMB_PATH"
      base64 -w 0 "$THUMB_PATH" >> "$TEMP_SQL"
      rm -f "$THUMB_PATH"
    else
      echo "Using original image as fallback"
      base64 -w 0 "$IMAGE_PATH" >> "$TEMP_SQL"
    fi

    echo "', 'base64') WHERE id = $CONTACT_ID;" >> "$TEMP_SQL"

    # Execute the image update
    PGPASSWORD="$POSTGRES_PASSWORD" psql -h db -U "$POSTGRES_USER" -d "$POSTGRES_DB" -f "$TEMP_SQL" > /dev/null

    # Clean up temporary file
    rm "$TEMP_SQL"

    echo "Contact $i inserted with image (ID: $CONTACT_ID)"
  else
    echo "Warning: Image file $IMAGE_PATH not found, inserting contact without image"
    # Insert contact with the description id but no image
    if [ -z "$NICKNAME" ]; then
      # Insert without nickname if it's empty
      PGPASSWORD="$POSTGRES_PASSWORD" psql -h db -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "INSERT INTO contacts (id, name, phone, birthday, notes, email, address, description_id) VALUES (nextval('contacts_seq'), '$NAME', '$PHONE', '$BIRTHDAY', '$NOTES', '$EMAIL', '$ADDRESS', $DESC_ID);"
    else
      # Insert with nickname
      PGPASSWORD="$POSTGRES_PASSWORD" psql -h db -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "INSERT INTO contacts (id, name, phone, nickname, birthday, notes, email, address, description_id) VALUES (nextval('contacts_seq'), '$NAME', '$PHONE', '$NICKNAME', '$BIRTHDAY', '$NOTES', '$EMAIL', '$ADDRESS', $DESC_ID);"
    fi
  fi
done