workspace "TextilFlow Platform - Spring Boot Backend" "Software architecture diagrams for the TextilFlow textile management application" {
    model {
        # People
        businessman = person "Businessman" "A person who manages textile batches and reviews suppliers"
        supplier = person "Supplier" "A person who provides textile services and receives reviews"

        # Software System
        textilflowPlatform = softwareSystem "TextilFlow Platform" "Allows businessmen to manage textile batches and suppliers to provide services with observation tracking" {

            # Main Containers
            webApplication = container "Web Application" "Delivers the static content and the TextilFlow single page application" "Angular, TypeScript" "webapp"

            singlePageApplication = container "Single Page Application" "Provides all textile management functionality to businessmen and suppliers via their web browser" "TypeScript, Angular, Angular Material" "spa" {
                tags "Web Browser"
            }

            apiApplication = container "API Application" "Provides textile management functionality via RESTful API - Modular Monolith" "Spring Boot, Java" "api" {
                tags "API"

                # Bounded Context Components
                iamContext = component "IAM Bounded Context" "Manages user authentication, authorization and user data" "Spring Boot Module"
                profilesContext = component "Profiles Bounded Context" "Manages businessman and supplier profile information" "Spring Boot Module"
                batchesContext = component "Batches Bounded Context" "Handles textile batch management and tracking" "Spring Boot Module"
                reviewsContext = component "Reviews Bounded Context" "Manages supplier reviews and ratings" "Spring Boot Module"
                observationsContext = component "Observations Bounded Context" "Handles batch observations and quality control" "Spring Boot Module"
                sharedKernel = component "Shared Kernel" "Provides shared utilities, abstractions and common services" "Spring Boot Module"
            }

            # IAM Bounded Context as Container for detailed view
            iamBoundedContext = container "IAM Bounded Context" "Manages user authentication, authorization and user data" "Spring Boot Module" {
                # Interface Layer Components
                userController = component "UserController" "REST API for user management" "Spring REST Controller"
                userAssembler = component "UserAssembler" "Converts between REST resources and domain models" "Spring Component"

                # Application Layer Components
                userCommandService = component "UserCommandService" "Handles user commands" "Spring Service"
                userQueryService = component "UserQueryService" "Handles user queries" "Spring Service"

                # Infrastructure Layer Components
                userRepository = component "UserRepository" "Data access for users" "Spring Data JPA Repository"
                hashingService = component "HashingService" "Password hashing service" "Spring Service"
                tokenService = component "TokenService" "JWT token management" "Spring Service"
                emailServiceImpl = component "EmailServiceImpl" "Email notification service implementation" "Spring Service"
            }

            # Profiles Bounded Context as Container for detailed view
            profilesBoundedContext = container "Profiles Bounded Context" "Manages businessman and supplier profile information" "Spring Boot Module" {
                # Interface Layer Components
                businessmanController = component "BusinessmanController" "REST API for businessman profiles" "Spring REST Controller"
                supplierController = component "SupplierController" "REST API for supplier profiles" "Spring REST Controller"
                profileAssembler = component "ProfileAssembler" "Converts between REST resources and domain models" "Spring Component"

                # Application Layer Components
                businessmanCommandService = component "BusinessmanCommandService" "Handles businessman commands" "Spring Service"
                businessmanQueryService = component "BusinessmanQueryService" "Handles businessman queries" "Spring Service"
                supplierCommandService = component "SupplierCommandService" "Handles supplier commands" "Spring Service"
                supplierQueryService = component "SupplierQueryService" "Handles supplier queries" "Spring Service"

                # Infrastructure Layer Components
                businessmanRepository = component "BusinessmanRepository" "Data access for businessmen" "Spring Data JPA Repository"
                supplierRepository = component "SupplierRepository" "Data access for suppliers" "Spring Data JPA Repository"
                cloudinaryServiceImpl = component "CloudinaryServiceImpl" "Image upload service implementation" "Spring Service"
                profilesContextFacade = component "ProfilesContextFacade" "ACL for other contexts" "Spring Component"
            }

            # Batches Bounded Context as Container for detailed view
            batchesBoundedContext = container "Batches Bounded Context" "Handles textile batch management and tracking" "Spring Boot Module" {
                # Interface Layer Components
                batchController = component "BatchController" "REST API for batch management" "Spring REST Controller"
                batchAssembler = component "BatchAssembler" "Converts between REST resources and domain models" "Spring Component"

                # Application Layer Components
                batchCommandService = component "BatchCommandService" "Handles batch commands" "Spring Service"
                batchQueryService = component "BatchQueryService" "Handles batch queries" "Spring Service"

                # Infrastructure Layer Components
                batchRepository = component "BatchRepository" "Data access for batches" "Spring Data JPA Repository"
            }

            # Reviews Bounded Context as Container for detailed view
            reviewsBoundedContext = container "Reviews Bounded Context" "Manages supplier reviews and ratings" "Spring Boot Module" {
                # Interface Layer Components
                supplierReviewController = component "SupplierReviewController" "REST API for supplier reviews" "Spring REST Controller"
                reviewAssembler = component "ReviewAssembler" "Converts between REST resources and domain models" "Spring Component"

                # Application Layer Components
                supplierReviewCommandService = component "SupplierReviewCommandService" "Handles review commands" "Spring Service"
                supplierReviewQueryService = component "SupplierReviewQueryService" "Handles review queries" "Spring Service"

                # Infrastructure Layer Components
                supplierReviewRepository = component "SupplierReviewRepository" "Data access for reviews" "Spring Data JPA Repository"
                externalProfilesService = component "ExternalProfilesService" "ACL to profiles context" "Spring Component"
            }

            # Observations Bounded Context as Container for detailed view
            observationsBoundedContext = container "Observations Bounded Context" "Handles batch observations and quality control" "Spring Boot Module" {
                # Interface Layer Components
                observationController = component "ObservationController" "REST API for observations" "Spring REST Controller"
                observationAssembler = component "ObservationAssembler" "Converts between REST resources and domain models" "Spring Component"

                # Application Layer Components
                observationCommandService = component "ObservationCommandService" "Handles observation commands" "Spring Service"
                observationQueryService = component "ObservationQueryService" "Handles observation queries" "Spring Service"

                # Infrastructure Layer Components
                observationRepository = component "ObservationRepository" "Data access for observations" "Spring Data JPA Repository"
            }

            # Shared Kernel as Container for detailed view
            sharedKernelContainer = container "Shared Kernel" "Provides shared utilities, abstractions and common services" "Spring Boot Module" {
                # Shared Infrastructure Components
                auditableAbstractAggregateRoot = component "AuditableAbstractAggregateRoot" "Base class for aggregate roots" "Spring Component"
                openApiConfiguration = component "OpenApiConfiguration" "API documentation configuration" "Spring Configuration"
                snakeCaseNamingStrategy = component "SnakeCasePhysicalNamingStrategy" "Database naming strategy" "Hibernate Strategy"
                cloudinaryService = component "CloudinaryService" "Interface for image upload service" "Spring Interface"
                emailService = component "EmailService" "Interface for email service" "Spring Interface"
            }

            database = container "Database" "Stores user information, batches, reviews, observations and related metadata" "MySQL Server" "database" {
                tags "Database"
            }
        }

        # External Systems
        cloudinarySystem = softwareSystem "Cloudinary" "External cloud service for image storage and management" "External System"
        emailSystem = softwareSystem "Email Service" "External email service for notifications" "External System"

        # Relationships between people and system
        businessman -> textilflowPlatform "Manages textile batches and reviews suppliers using"
        supplier -> textilflowPlatform "Provides services and manages observations using"

        # Container level relationships
        businessman -> webApplication "Visits textilflow.com using" "HTTPS"
        supplier -> webApplication "Visits textilflow.com using" "HTTPS"

        webApplication -> singlePageApplication "Delivers to the customer's web browser"
        singlePageApplication -> apiApplication "Makes API calls to" "JSON/HTTPS" "REST API"

        # API Application to Database
        apiApplication -> database "Reads from and writes to" "Spring Data JPA"
        apiApplication -> cloudinarySystem "Uploads and manages images" "HTTPS/REST"
        apiApplication -> emailSystem "Sends welcome emails" "SMTP"

        # Bounded Context level relationships (simplified for main view)
        iamContext -> database "Reads from and writes to"
        profilesContext -> database "Reads from and writes to"
        batchesContext -> database "Reads from and writes to"
        reviewsContext -> database "Reads from and writes to"
        observationsContext -> database "Reads from and writes to"

        profilesContext -> cloudinarySystem "Uploads images"
        iamContext -> emailSystem "Sends emails"

        batchesContext -> profilesContext "Validates existence"
        reviewsContext -> profilesContext "Gets profile info"
        profilesContext -> iamContext "Creates profiles"

        iamContext -> sharedKernel "Uses"
        profilesContext -> sharedKernel "Uses"
        batchesContext -> sharedKernel "Uses"
        reviewsContext -> sharedKernel "Uses"
        observationsContext -> sharedKernel "Uses"

        # Detailed IAM relationships
        userController -> userCommandService "Uses"
        userController -> userQueryService "Uses"
        userController -> userAssembler "Uses"
        userCommandService -> userRepository "Uses"
        userCommandService -> hashingService "Uses"
        userCommandService -> tokenService "Uses"
        userCommandService -> emailServiceImpl "Uses"
        userQueryService -> userRepository "Uses"
        userRepository -> database "Reads from and writes to"
        emailServiceImpl -> emailSystem "Sends emails to"

        # Detailed Profiles relationships
        businessmanController -> businessmanCommandService "Uses"
        businessmanController -> businessmanQueryService "Uses"
        businessmanController -> profileAssembler "Uses"
        supplierController -> supplierCommandService "Uses"
        supplierController -> supplierQueryService "Uses"
        supplierController -> profileAssembler "Uses"
        businessmanCommandService -> businessmanRepository "Uses"
        businessmanCommandService -> cloudinaryServiceImpl "Uses"
        businessmanQueryService -> businessmanRepository "Uses"
        supplierCommandService -> supplierRepository "Uses"
        supplierCommandService -> cloudinaryServiceImpl "Uses"
        supplierQueryService -> supplierRepository "Uses"
        businessmanRepository -> database "Reads from and writes to"
        supplierRepository -> database "Reads from and writes to"
        cloudinaryServiceImpl -> cloudinarySystem "Uploads images to"

        # Detailed Batches relationships
        batchController -> batchCommandService "Uses"
        batchController -> batchQueryService "Uses"
        batchController -> batchAssembler "Uses"
        batchCommandService -> batchRepository "Uses"
        batchCommandService -> profilesContextFacade "Uses"
        batchQueryService -> batchRepository "Uses"
        batchRepository -> database "Reads from and writes to"

        # Detailed Reviews relationships
        supplierReviewController -> supplierReviewCommandService "Uses"
        supplierReviewController -> supplierReviewQueryService "Uses"
        supplierReviewController -> reviewAssembler "Uses"
        supplierReviewCommandService -> supplierReviewRepository "Uses"
        supplierReviewQueryService -> supplierReviewRepository "Uses"
        externalProfilesService -> profilesContextFacade "Uses"
        supplierReviewRepository -> database "Reads from and writes to"

        # Detailed Observations relationships
        observationController -> observationCommandService "Uses"
        observationController -> observationQueryService "Uses"
        observationController -> observationAssembler "Uses"
        observationCommandService -> observationRepository "Uses"
        observationQueryService -> observationRepository "Uses"
        observationRepository -> database "Reads from and writes to"

        # Shared Kernel relationships
        cloudinaryServiceImpl -> cloudinaryService "Implements"
        emailServiceImpl -> emailService "Implements"
    }

    views {
        systemContext textilflowPlatform "SystemContext" "The system context diagram for the TextilFlow Platform" {
            include *
            autoLayout lr
        }

        container textilflowPlatform "Containers" "The container diagram for the TextilFlow Platform" {
            include webApplication
            include singlePageApplication
            include apiApplication
            include database
            include cloudinarySystem
            include emailSystem
            include businessman
            include supplier
            autoLayout lr
        }

        component apiApplication "API-Components" "The component diagram showing bounded contexts within the API Application" {
            include apiApplication
            include iamContext
            include profilesContext
            include batchesContext
            include reviewsContext
            include observationsContext
            include sharedKernel
            include database
            include cloudinarySystem
            include emailSystem
            autoLayout
        }

        # Detailed Component Diagrams for each Bounded Context
        component iamBoundedContext "IAM-Components" "Components within the IAM Bounded Context" {
            include *
            autoLayout
        }

        component profilesBoundedContext "Profiles-Components" "Components within the Profiles Bounded Context" {
            include *
            autoLayout
        }

        component batchesBoundedContext "Batches-Components" "Components within the Batches Bounded Context" {
            include *
            autoLayout
        }

        component reviewsBoundedContext "Reviews-Components" "Components within the Reviews Bounded Context" {
            include *
            autoLayout
        }

        component observationsBoundedContext "Observations-Components" "Components within the Observations Bounded Context" {
            include *
            autoLayout
        }

        component sharedKernelContainer "SharedKernel-Components" "Components within the Shared Kernel" {
            include *
            autoLayout
        }

        styles {
            element "Person" {
                shape "Person"
                background "#08427b"
                color "#ffffff"
            }
            element "Software System" {
                background "#1168bd"
                color "#ffffff"
            }
            element "External System" {
                background "#999999"
                color "#ffffff"
            }
            element "Container" {
                background "#438dd5"
                color "#ffffff"
            }
            element "Web Browser" {
                shape "WebBrowser"
                background "#438dd5"
                color "#ffffff"
            }
            element "API" {
                background "#438dd5"
                color "#ffffff"
            }
            element "Database" {
                shape "Cylinder"
                background "#438dd5"
                color "#ffffff"
            }
            element "Component" {
                background "#85bbf0"
                color "#000000"
            }
        }

        theme default
    }

    properties {
        structurizr.groupSeparator "/"
    }
}