CREATE DATABASE posture_annotation_db;

\c posture_annotation_db;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    password TEXT NOT NULL,
    token TEXT NOT NULL,
    created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE postures (
    id SERIAL PRIMARY KEY,
    in_id INTEGER NOT NULL,
    file_name TEXT NOT NULL,
    set_num INTEGER NOT NULL,
    orientation_alpha REAL NOT NULL,
    orientation_beta REAL NOT NULL,
    orientation_gamma REAL NOT NULL,
    pitch REAL NOT NULL,
    yaw REAL NOT NULL,
    roll REAL NOT NULL,
    nose_x REAL NOT NULL,
    nose_y REAL NOT NULL,
    neck_x REAL NOT NULL,
    neck_y REAL NOT NULL,
    neck_to_nose REAL NOT NULL,
    standard_dist REAL NOT NULL,
    in_created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    in_updated_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    user_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    password TEXT NOT NULL,
    neck_to_nose_standard REAL NOT NULL,
    neck_angle_offset REAL NOT NULL,
    ex_id INTEGER NOT NULL,
    neck_angle REAL NOT NULL,
    torso_angle REAL NOT NULL,
    tragus_x REAL DEFAULT NULL,
    tragus_y REAL DEFAULT NULL,
    shoulder_x REAL DEFAULT NULL,
    shoulder_y REAL DEFAULT NULL,
    waist_x REAL DEFAULT NULL,
    waist_y REAL DEFAULT NULL,
    image_width INTEGER DEFAULT NULL,
    image_height INTEGER DEFAULT NULL,
    ex_created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    is_sample BOOLEAN DEFAULT FALSE,
    updated_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE annotations (
    id SERIAL PRIMARY KEY,
    posture_id INTEGER NOT NULL,
    annotater_id INTEGER NOT NULL,
    neck_angle REAL NOT NULL,
    torso_angle REAL NOT NULL,
    tragus_x REAL DEFAULT NULL,
    tragus_y REAL DEFAULT NULL,
    shoulder_x REAL DEFAULT NULL,
    shoulder_y REAL DEFAULT NULL,
    waist_x REAL DEFAULT NULL,
    waist_y REAL DEFAULT NULL,
    created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_postures_updated_at
BEFORE UPDATE ON postures
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_annotations_updated_at
BEFORE UPDATE ON annotations
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
