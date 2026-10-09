# Limitations

## Planning aid only

This application is an advisory geometry and planning tool. It does not
replace the crane manufacturer's instructions, load chart, LMI/RCL,
engineered lift plan, competent-person assessment, applicable regulations,
or site-specific risk assessment.

## Simplified geometry

R = L * cos(theta)

Does not account for boom pivot offsets, telescopic sections, jibs,
attachments, deflection, or ground slope.

## No world tracking

CameraX does not provide world tracking. Gyroscope and accelerometer data
do not independently provide reliable absolute 3D position.

## No depth from single images

The phone cannot calculate arbitrary real-world depth from one monocular
image without additional information. Point selections are 2D image
coordinates only.

## Not a fake AR system

This application does not implement ARCore or any unvalidated tracking.
